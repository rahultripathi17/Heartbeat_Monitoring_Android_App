package com.heartmonitoring.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class PdfGenerateActivity extends AppCompatActivity {


    
    
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    List<RecordingModel> list;
    SimpleDateFormat timeFormat,dateFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_generate);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        timeFormat = new SimpleDateFormat("hh:mm a");
        list = (List<RecordingModel>) getIntent().getExtras().get("list");

        
        sessionManager = new SessionManager(PdfGenerateActivity.this);
        progressDialog = new ProgressDialog(PdfGenerateActivity.this);

        try {
            createPdf();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    private void createPdf() throws FileNotFoundException {
        progressDialog.show();


        String name = sessionManager.getUsersDetailsFromSessions().get(SessionManager.KEY_USERNAME);
        String phone = sessionManager.getUsersDetailsFromSessions().get(SessionManager.KEY_EMAIL);
        String uid = sessionManager.getUsersDetailsFromSessions().get(SessionManager.KEY_UID);
        long ts = Calendar.getInstance().getTime().getTime();

        File file = new File(getExternalFilesDir(null),  "HeartBeat Monitor Audio Report  " + phone.trim()+".pdf");
        OutputStream outputStream = new FileOutputStream(file);

        DeviceRgb stripColor = new DeviceRgb(222,223,238);
        DeviceRgb invoiceBlue = new DeviceRgb(56, 182, 255);

        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        document.setFontSize(12);

        float[] columnWidth = new float[]{140, 140, 140, 140};


        Table table1 = new Table(columnWidth);


        Drawable d1 = getDrawable(R.drawable.health_monitoring);
        Bitmap bitmap1 = ((BitmapDrawable) d1).getBitmap();
        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();

        bitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream1);

        byte[] bitmapData1 = stream1.toByteArray();

        ImageData ImageData1 = ImageDataFactory.create(bitmapData1);
        Image image1 = new Image(ImageData1);
        image1.setWidth(100f);


        //Table1 00
        table1.addCell(new Cell(4, 4).add(new Paragraph()).setBorder(Border.NO_BORDER));

        table1.addCell(new Cell(4, 4).add(new Paragraph("Heart Monitoring Report").setTextAlignment(TextAlignment.CENTER).setBold().setFontSize(15)).setBorder(Border.NO_BORDER));

        table1.addCell(new Cell(4, 4).add(new Paragraph()).setBorder(Border.NO_BORDER));


        //Table1 00
        table1.addCell(new Cell(4, 2).add(image1).setBorder(Border.NO_BORDER));
        //table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));

        //Table1 01
        //table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        //table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));

        //Table1 02
        //table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        //table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell(1, 2).add(new Paragraph("UID : " + uid.trim()).setTextAlignment(TextAlignment.RIGHT).setFontSize(8)).setBorder(Border.NO_BORDER));
        //table1.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));


        //Table1 03
        //table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        //table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell(1, 2).add(new Paragraph("Date & Time : " + dateFormat.format(ts) + " at " + timeFormat.format(ts)).setFontSize(8).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
        //table1.addCell(new Cell().add(new Paragraph("abcdefghij1234")).setBorder(Border.NO_BORDER));


        //Table1 04
        table1.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));


        //Table1 05
        table1.addCell(new Cell(4, 4).add(new Paragraph("Name : "+name + "\n").setBold()).add(new Paragraph("Email : "+phone)).setBorder(Border.NO_BORDER));
        //table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        //table1.addCell(new Cell(4, 2).add(new Paragraph("GearToCare\n").setBold().setTextAlignment(TextAlignment.RIGHT)).add(new Paragraph("8888142447\ngeartocare@gmail.com\nwww.geartocare.com").setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
        //table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));

        


        float[] columnWidth2 = {140, 100, 220, 100};
        Table table2 = new Table(columnWidth2);

        table2.setFontSize(10);

        //Table 2 Row 0
        table2.addCell(new Cell().add(new Paragraph("Name").setFontColor(ColorConstants.WHITE).setFontSize(8)).setBackgroundColor(invoiceBlue));
        table2.addCell(new Cell().add(new Paragraph("Date").setFontColor(ColorConstants.WHITE).setFontSize(8)).setBackgroundColor(invoiceBlue));
        table2.addCell(new Cell().add(new Paragraph("Note").setFontColor(ColorConstants.WHITE).setFontSize(8)).setBackgroundColor(invoiceBlue));
        table2.addCell(new Cell().add(new Paragraph("Link").setFontColor(ColorConstants.WHITE).setFontSize(8)).setBackgroundColor(invoiceBlue));

        Integer totalSaving = 0;

        for (RecordingModel r : list) {

            if(r.getName()!=null){
                table2.addCell(new Cell().add(new Paragraph(r.getName())));
            }else{
                table2.addCell(new Cell().add(new Paragraph("None")));
            }

            table2.addCell(new Cell().add(new Paragraph(dateFormat.format(r.getTimeStamp()))));
            table2.addCell(new Cell().add(new Paragraph(r.getNote())));
            // Create a new Link object with the URL for the hyperlink
            Link link = new Link("Link", PdfAction.createURI(r.getLink()));

            table2.addCell(new Cell().add(new Paragraph(link).setUnderline()));
        }

        table2.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER).setBackgroundColor(stripColor));
        table2.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER).setBackgroundColor(stripColor));
        table2.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER).setBackgroundColor(stripColor));
        table2.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER).setBackgroundColor(stripColor));





//  //Table 2 Row 6
//        table2.addCell(new Cell().add(new Paragraph()));
//        table2.addCell(new Cell().add(new Paragraph()));
//        table2.addCell(new Cell().add(new Paragraph()));
//        table2.addCell(new Cell().add(new Paragraph("Sub Total")));
//        table2.addCell(new Cell().add(new Paragraph("1000")));
//
//  //Table 2 Row 7
//        table2.addCell(new Cell(1,2).add(new Paragraph("Terms and Conditions")));
//        //table2.addCell(new Cell().add(new Paragraph()));
//        table2.addCell(new Cell().add(new Paragraph()));
//        table2.addCell(new Cell().add(new Paragraph("GST 12%")));
//        table2.addCell(new Cell().add(new Paragraph("132")));
//
//  //Table 2 Row 8
//        table2.addCell(new Cell().add(new Paragraph("Oh Yeah")));
//        table2.addCell(new Cell().add(new Paragraph()));
//        table2.addCell(new Cell().add(new Paragraph()));
//        table2.addCell(new Cell().add(new Paragraph("Grand Total")));
//        table2.addCell(new Cell().add(new Paragraph("1500")));


        float[] columnWidth3 = {280,180, 100};
        Table table3 = new Table(columnWidth3);


        Drawable d2 = getDrawable(R.drawable.health_monitoring);
        Bitmap bitmap2 = ((BitmapDrawable) d2).getBitmap();
        ByteArrayOutputStream stream2 = new ByteArrayOutputStream();

        bitmap2.compress(Bitmap.CompressFormat.PNG, 100, stream2);

        byte[] bitmapData2 = stream2.toByteArray();

        ImageData ImageData2 = ImageDataFactory.create(bitmapData2);
        Image image2 = new Image(ImageData2);
        image2.setHeight(40);
        image2.setHorizontalAlignment(HorizontalAlignment.RIGHT);




        table3.addCell(new Cell(1, 4).add(new Paragraph("Generated using HeartBeat Monitoring Android Application").setBold()).setBorder(Border.NO_BORDER));
        table3.addCell(new Cell(1, 4).add(new Paragraph("Project created by : Rahul Tripathi")).setBorder(Border.NO_BORDER));
        //table3.addCell(new Cell(1, 1).add(image2.setHorizontalAlignment(HorizontalAlignment.CENTER)).setBorder(Border.NO_BORDER).add(new Paragraph("Authorized Sign").setTextAlignment(TextAlignment.CENTER)).setHorizontalAlignment(HorizontalAlignment.RIGHT));
        //table3.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        //table3.addCell(new Cell().add(new Paragraph("Service Regularly and Drive Safely")).setBorder(Border.NO_BORDER));
        //table3.addCell(new Cell().add(new Paragraph("Authorized Sign").setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));


        document.add(table1);
        document.add(new Paragraph("\n"));
        document.add(table2);
        document.add(new Paragraph("\n"));
        document.add(table3);
        //document.add(new Paragraph("Authorized Sign").setTextAlignment(TextAlignment.RIGHT));
        document.close();

        Uri uri = FileProvider.getUriForFile(PdfGenerateActivity.this, getApplicationContext().getPackageName() + ".provider", file);
        Log.i("checkWithLog", uri.toString());

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("application/pdf");

        /*intent.putExtra("jid", "@s.whatsapp.net");
        intent.setPackage("com.whatsapp");
        startActivity(intent);*/


        Intent shareIntent = Intent.createChooser(intent, "Share PDF via");
        startActivity(shareIntent);

        progressDialog.dismiss();
        finish();
    }
}