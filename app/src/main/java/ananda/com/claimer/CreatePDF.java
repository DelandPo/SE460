package ananda.com.claimer;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Daniel Tillmann 4/10/2018
 * **/

public class CreatePDF extends AppCompatActivity {

    public static final String DEST = Environment.getExternalStorageDirectory() + "/Documents";
    private static final String TAG = "PdfCreatorActivity";
    //public static final String IMG = "/storage/emulated/0/Download/Img.jpg";
    private final int REQUEST_CODE_ASK_PERMISSIONS = 111;
    public Uri outputFileUri;
    public Context context = this;
    private EditText  emailAddress;
    private Button mCreateButton;
    private File pdfFile;
    private Image img;

    private DataAccessLayer data;
    private ArrayList<Items> items;

    private String serialNumber, modelNumber, itemName, room;



    protected void onCreate(Bundle savedInstanceState) {
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/claimer/";
        Log.d("Directory",dir);
        File newdir = new File(dir);
        newdir.mkdirs();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfcreator);
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        items = data.GetAllItems();
        emailAddress = (EditText) findViewById(R.id.txtEmail);
        mCreateButton = (Button) findViewById(R.id.button_create);
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String reportFile = dir+ timeStamp + ".pdf";
        File newfile = new File(reportFile);
        try {
            newfile.createNewFile();
        }
        catch (IOException e)
        {
        }
        outputFileUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider",newfile);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (emailAddress.getText().toString().isEmpty()){
                    emailAddress.setError("No Address");
                    emailAddress.requestFocus();
                    return;
                }

                try {

                    createPdfWrapper();
                    Log.d("Email log", "Email");
                }catch(IOException io){
                    io.printStackTrace();
                }catch(DocumentException d){
                    d.printStackTrace();
                }
            }
        });

    }


    private void createPdfWrapper() throws FileNotFoundException, DocumentException{
        Toast.makeText(this, "Calling ceatePdfWrapper", Toast.LENGTH_SHORT);
        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("You need to allow access to Storage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS
                                        );
                                    }
                                }
                            });
                    return;
                }

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        }else {
            sendEmail();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    try {
                        createPdfWrapper();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Permission Denied
                    Toast.makeText(this, "WRITE_EXTERNAL Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public void createPdf() throws IOException, DocumentException {
        /*File docsFolder = new File(Environment.getExternalStorageDirectory() + "/SE460");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i(TAG, "Created a new directory for PDF");
        }
        */
        pdfFile = new File(outputFileUri.getPath());
        OutputStream output = new FileOutputStream(pdfFile);


        Paragraph line = new Paragraph();
        Document document = new Document();
        PdfWriter.getInstance(document, output);
        document.open();


        /** ADD CONTENT TO PDF HERE**/

        for(int i = 0; i<= items.size(); i++) {

            try {
             img = Image.getInstance(items.get(i).getPictureURL());
                img.setAlt("IMG");
                img.scaleAbsolute(128, 72);
                img.setRotationDegrees(-90);
                line.add(img);
                line.add(new Chunk("Serial Number: " + items.get(i).getSerialNumber() + "\n"));
                line.add(new Chunk("Product Number: " + items.get(i).getModelNumber() + "\n"));
                line.add(new Chunk("Room: " + items.get(i).getRoomType() + "\n"));
                line.add(new Chunk("Name: Hello World"));
                int itemCounter = 1;
                    if (itemCounter == 4) {
                        document.newPage(); // Add a new page once the current page has three items on it.
                        itemCounter = 1; // reset the counter to begin at one on the newly added page.
                    }
                    document.add(line);
                    itemCounter++;

            } catch (DocumentException d) {
                d.printStackTrace();
                Toast.makeText(this, "IMG not found", Toast.LENGTH_SHORT).show();
            }
        }
        document.close();
        Toast.makeText(this,"PDF generated", Toast.LENGTH_SHORT).show();

        sendEmail();

        //development purposes only.
        //previewPdf();
    }


    private void sendEmail(){
        StringBuilder sb = new StringBuilder();
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        for (Items itms:DataAccessLayer.GetAllItems()
             ) {
            sb.append(itms.ItemDetails());
            sb.append("\n\n");
        }


        String emailBody = "Items Inventory\n\n" +  sb.toString();
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {emailAddress.getText().toString()}); // recipients
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Inventory List");
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);
        //emailIntent.putExtra(Intent.EXTRA_STREAM, outputFileUri);

        Intent chooser = new Intent().createChooser(emailIntent, "Share");

        if(emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }

        Log.d("Send Email function", "Email");
    }

    private void previewPdf() {

        PackageManager packageManager = getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(pdfFile);
            intent.setDataAndType(uri, "application/pdf");

            startActivity(intent);
        }else{
            Toast.makeText(this,"Download a PDF Viewer to see the generated PDF",Toast.LENGTH_SHORT).show();
        }
    }
}