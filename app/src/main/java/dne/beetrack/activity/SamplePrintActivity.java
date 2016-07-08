package dne.beetrack.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brother.ptouch.sdk.Printer;
import com.brother.ptouch.sdk.PrinterInfo;

import java.io.File;
import java.io.FileOutputStream;

import dne.beetrack.R;
import dne.beetrack.print.Activity_PrintImage;
import dne.beetrack.print.Activity_PrinterPreference;
import dne.beetrack.print.common.Common;
import dne.beetrack.print.printprocess.PrinterModelInfo;

/**
 * Created by USER on 06/16/2016.
 */
public class SamplePrintActivity extends MyBaseActivity implements View.OnClickListener {

    private String NAME_PATH = "TestPrinter";

    private Button btnPrinter;
    private Button btnSetting;
    private RelativeLayout rltText;
    private TextView txtToPrint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_print);

        initView();
        initData();
    }

    private void initView() {
        btnPrinter = (Button) findViewById(R.id.btnPrinter);
        btnSetting = (Button) findViewById(R.id.btnSetting);
        rltText = (RelativeLayout) findViewById(R.id.rltText);
        txtToPrint = (TextView) findViewById(R.id.txtToPrint);

        btnPrinter.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
    }

    private void initData() {
        setPrefereces();
        String code = getIntent().getStringExtra("code");
        txtToPrint.setText(code);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPrinter:
//                Intent intent = new Intent(this, Activity_PrintImage.class);
//                startActivity(intent);
                SaveImageAsync saveImageAsync = new SaveImageAsync();
                saveImageAsync.execute();
                break;
            case R.id.btnSetting:
                Intent intentSetting = new Intent(this, Activity_PrinterPreference.class);
                startActivity(intentSetting);
                break;
        }
    }

    private class SaveImageAsync extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            showProgressDialog(false);
        }

        @Override
        protected String doInBackground(Void... params) {
            Bitmap bitmap = convertViewToBitmap();
            String filename = null;
            filename = saveBitmapToSDCard(bitmap);
            return filename;
        }

        @Override
        protected void onPostExecute(String result) {
            hideProgressDialog();
            if (result != null) {
                showToastInfo("Saved");
                Intent intent = new Intent(SamplePrintActivity.this, Activity_PrintImage.class);
                intent.putExtra(Common.INTENT_FILE_NAME, result);
                startActivity(intent);
            } else {
                showToastInfo("Save fail");
            }
        }
    }

    private Bitmap convertViewToBitmap() {
        rltText.setDrawingCacheEnabled(false);
        rltText.destroyDrawingCache();
        rltText.buildDrawingCache();
        return rltText.getDrawingCache();
    }

    private String saveBitmapToSDCard(Bitmap bitmap) {
        String imageName = NAME_PATH + "_" + System.currentTimeMillis();
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/" + NAME_PATH);

        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        String fname = imageName + ".png";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            ContentValues image = new ContentValues();
            image.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, image);
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {

        }
    }

    private void setPrefereces() {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        // initialization for print
        PrinterInfo printerInfo = new PrinterInfo();
        Printer printer = new Printer();
        printerInfo = printer.getPrinterInfo();
        if (printerInfo == null) {
            printerInfo = new PrinterInfo();
            printer.setPrinterInfo(printerInfo);

        }
        if (sharedPreferences.getString("printerModel", "").equals("")) {
            String printerModel = printerInfo.printerModel.toString();
            PrinterModelInfo.Model model = PrinterModelInfo.Model.valueOf(printerModel);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("printerModel", printerModel);
            editor.putString("port", printerInfo.port.toString());
            editor.putString("address", printerInfo.ipAddress.toString());
            editor.putString("macAddress", printerInfo.macAddress.toString());

            // Override SDK default paper size
            editor.putString("paperSize", model.getDefaultPaperSize());

            editor.putString("orientation", printerInfo.orientation.toString());
            editor.putString("numberOfCopies",
                    Integer.toString(printerInfo.numberOfCopies));
            editor.putString("halftone", printerInfo.halftone.toString());
            editor.putString("printMode", printerInfo.printMode.toString());
            editor.putString("pjCarbon", Boolean.toString(printerInfo.pjCarbon));
            editor.putString("pjDensity",
                    Integer.toString(printerInfo.pjDensity));
            editor.putString("pjFeedMode", printerInfo.pjFeedMode.toString());
            editor.putString("align", printerInfo.align.toString());
            editor.putString("leftMargin",
                    Integer.toString(printerInfo.margin.left));
            editor.putString("valign", printerInfo.valign.toString());
            editor.putString("topMargin",
                    Integer.toString(printerInfo.margin.top));
            editor.putString("customPaperWidth",
                    Integer.toString(printerInfo.customPaperWidth));
            editor.putString("customPaperLength",
                    Integer.toString(printerInfo.customPaperLength));
            editor.putString("customFeed",
                    Integer.toString(printerInfo.customFeed));
            editor.putString("paperPostion",
                    printerInfo.paperPosition.toString());
            editor.putString("customSetting",
                    sharedPreferences.getString("customSetting", ""));
            editor.putString("rjDensity",
                    Integer.toString(printerInfo.rjDensity));
            editor.putString("rotate180",
                    Boolean.toString(printerInfo.rotate180));
            editor.putString("dashLine", Boolean.toString(printerInfo.dashLine));
            editor.putString("peelMode", Boolean.toString(printerInfo.peelMode));
            editor.putString("mode9", Boolean.toString(printerInfo.mode9));
            editor.putString("pjSpeed", Integer.toString(printerInfo.pjSpeed));

            editor.putString("printerCase",
                    printerInfo.rollPrinterCase.toString());
            editor.putString("skipStatusCheck",
                    Boolean.toString(printerInfo.skipStatusCheck));
            editor.putString("checkPrintEnd", printerInfo.checkPrintEnd.toString());

            editor.putString("imageThresholding",
                    Integer.toString(printerInfo.thresholdingValue));
            editor.putString("scaleValue",
                    Double.toString(printerInfo.scaleValue));

            editor.commit();
        }
    }
}
