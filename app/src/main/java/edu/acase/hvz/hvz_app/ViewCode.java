package edu.acase.hvz.hvz_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.Random;


/** This activity shows the human player their code so that they can reveal it to
 * zombies whenever they are tagged. Zombies can enter or scan this code, which will
 * cause the human to turn into a zombie (they will first incubate). */

public class ViewCode extends AppCompatActivity {

    ImageView imageView;
    Thread thread ;
    public final static int QRcodeWidth = 500 ;
    Bitmap bitmap ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_code);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String code = getToken();

        TextView viewC = (TextView) findViewById(R.id.textView);
        viewC.setText(code);

        imageView = (ImageView)findViewById(R.id.imageView);
        try {
            bitmap = TextToImageEncode(code);

            imageView.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }

        final Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(), IncubatingActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Generates the random token for the zombie to scan using stringbuilder and the random class
     * @return
     */
    public String getToken() {
        Random random = new Random();
        String CHARS = "ABCDEFGHJKLMNOPQRSTUVWXYZ234567890";
        StringBuilder token = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            token.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return token.toString();
    }

    /**
     * Takes in the random token from above
     * Uses multiformatWriter to encode
     * Takes class methods from ZXing class
     * Uses Bitmatrix width and height
     * @param Value
     * @return
     * @throws WriterException
     */
    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        ContextCompat.getColor(ViewCode.this, R.color.colorPrimaryDark):ContextCompat.getColor(ViewCode.this, R.color.blackTransparent);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}

