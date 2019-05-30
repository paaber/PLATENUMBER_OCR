package com.botosoft.frsc_plate_number_ocr;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.botosoft.util.PlateNumberDetect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Integer.parseInt;

public class Scan_Plate_Number extends AppCompatActivity {


    private ImageView imgCapture;
    private static final int Image_Capture_Code = 1;
    private String[] sError;
    private JSONObject jsonObject ;
    private String plateNumber, carModel,confidence,currentPhotoPath;
    private String cls = Scan_Plate_Number.class.getName();
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_plate_number);

        ImageButton btnCapture = findViewById(R.id.btnTakePicture);
        imgCapture = findViewById(R.id.capturedImage);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Image_Capture_Code) {
            if (resultCode == RESULT_OK) {
//                Bitmap bp = (Bitmap) data.getExtras().get("data");
//                Bundle extras = data.getExtras();
//                Bitmap imageBitmap = (Bitmap) extras.get("data");
                setPic();
                final PlateNumberDetect getPlateNumber = new PlateNumberDetect();
//                final Bitmap bitmap = ((BitmapDrawable) imgCapture.getDrawable()).getBitmap();
                ConstraintLayout container = findViewById(R.id.container);
                View inflatedLayout= getLayoutInflater().inflate(R.layout.progressload, null, false);
                container.addView(inflatedLayout);
                ProgressBar pgbar = findViewById(R.id.pgbar);
                pgbar.getIndeterminateDrawable().setColorFilter(0xFFFAFAFA, android.graphics.PorterDuff.Mode.MULTIPLY);
                pgbar.setVisibility(View.VISIBLE);
//                imgCapture.setVisibility(View.INVISIBLE);
                new Thread(new Runnable() {
                    public void run() {
//
                        sError = getPlateNumber.AnalyImage(bitmap);
//
                        try {

                            jsonObject = new JSONObject(sError[0]);
                            JSONArray result  = jsonObject.getJSONArray("results");

                            for (int i=0; i < result.length(); i++) {
                                JSONObject obj = result.getJSONObject(i);
                                plateNumber = obj.getString("plate");
                                confidence  = obj.getString("confidence");
//
//                                JSONObject obj2 = obj.getJSONObject("vehicle");
//                                JSONArray  obj2Arr = obj2.getJSONArray("color");
//                                JSONObject objColor = obj2Arr.getJSONObject(0);
//                                carColor = objColor.getString("name");
//                                JSONArray  obj2Arr2 = obj2.getJSONArray("make");
//                                JSONObject objMake = obj2Arr2.getJSONObject(0);
//                                carModel = objMake.getString("name");
//                                Log.d("ton", "run: " + plateNumber + " " + carModel + " " + carColor);

                            }

                            if(result.length() != 0 )
                            {
                                Intent in = new Intent(Scan_Plate_Number.this,Drivers_Details.class);
                                in.putExtra("plateNumber",plateNumber);
//                                in.putExtra("carColor",carColor);
//                                in.putExtra("carModel",carModel);
                                startActivity(in);

                            }
                            else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new AlertDialog.Builder(Scan_Plate_Number.this)
                                                .setTitle("Alert")
                                                .setMessage("You need to take good and well positioned pictures to get good result \n please take pictures again")

                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // Continue with delete operation
                                                    }
                                                })

                                                // A null listener allows the button to dismiss the dialog and take no further action.
//                                        .setNegativeButton(android.R.string.no, null)
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .show();

                                    }
                                });



                            }

//                                Log.d("ton", "run:  " + platenumber);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }).start();

//                TextView pltxt = (TextView)findViewById(R.id.pltxt);
//                pltxt.setText(sError);
//                ImageView secIm = (ImageView)findViewById(R.id.capturedImage2);
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
////        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
//                byte[] imageBytes = baos.toByteArray();
//                //decode base64 string to image
//                imageBytes = Base64.decode(sError[1], Base64.DEFAULT);
//                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//                secIm.setImageBitmap(decodedImage);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.botosoft.frsc_plate_number_ocr.Scan_Plate_Number",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, Image_Capture_Code);
            }
        }
    }
    private void setPic() {

        // Get the dimensions of the View
        int targetW = imgCapture.getWidth();
        int targetH = imgCapture.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        imgCapture.setImageBitmap(bitmap);

    }
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }



}
