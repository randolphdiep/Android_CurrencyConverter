package eu.rafalsiuta.simplecalc;
/**
 * @author Rafal Siuta
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;

public class Logic extends Activity {

    private static String newText;
    private static String oldText = "0";
    private static boolean hasDot = false;
    private static int dotCount = 0;

    private static DecimalFormat numberFormat;

    public static void setTxt(TextView display, double value, DecimalFormat numberFormat) {
        String result = String.valueOf(numberFormat.format(value));
        if (result.length() >= 19)
            display.setTextSize(22.0F);
        else
            display.setTextSize(28.0F);
        display.setText(result);
    }

    public static double parser(TextView display) {
        return Double.parseDouble(display.getText().toString().replaceAll(",", ""));
    }

    public static void onNumbers(View v, TextView display, DecimalFormat numberFormat) {
        newText = ((Button) v).getText().toString();
        oldText = display.getText().toString();
        try {
            if (oldText.length() == 15)
                oldText = display.getText().toString();
            else {
                if ("0".equals(oldText)) {
                    setTxt(display, Double.parseDouble(newText), numberFormat);
                } else {
                    if (hasDot) {
                        if (dotCount != 2 && !"0".equals(newText)) {
                            dotCount++;
                            setTxt(display, Double.parseDouble(oldText.replaceAll(",", "") + newText), numberFormat);
                        }
                    } else
                        setTxt(display, Double.parseDouble(oldText.replaceAll(",", "") + newText), numberFormat);
                }
            }

        } catch (Exception ex) {
        }
    }


    public static void onDecimal(View v, TextView display) {
        newText = ((Button) v).getText().toString();
        oldText = display.getText().toString();

        if (oldText.contains("."))
            oldText = display.getText().toString();
        else {
            display.setText((oldText + newText));
            hasDot = true;
        }
    }

    public static void onEqual(TextView input, TextView output, String currentRate, DecimalFormat numberFormat) {
        try {
            Double a = parser(input);
            Double b = Double.parseDouble(currentRate.replaceAll(",", "."));
            Double result = a * b;
            setTxt(output, result, numberFormat);
        } catch (Exception ex) {
            output.setText("0");
            output.setTextSize(28.0F);
        }
    }

    public static void onClear(TextView display, TextView output) {
        display.setText(newText = "0");
        output.setText("0");
        output.setTextSize(28.0F);
        dotCount = 0;
        hasDot = false;
    }

    public static void onSwap(Context context, TextView codeInput, TextView codeOutput, ImageView imageFrom, ImageView imageTo) {
        Drawable imageFromDraw = imageFrom.getDrawable();
        Glide.with(context).load(imageTo.getDrawable()).into(imageFrom);
        Glide.with(context).load(imageFromDraw).into(imageTo);
        String temp = codeInput.getText().toString();
        codeInput.setText(codeOutput.getText().toString());
        codeOutput.setText(temp);
    }

    public static void onDelete(TextView display, TextView output) {
        try {
            int start = 0;
            int end = display.getText().toString().length() - 1;
            output.setText("0");
            output.setTextSize(28.0F);
            if (display.getText().toString().length() > 0) {
                if (hasDot)
                    if (dotCount > 0)
                        dotCount--;
                    else {
                        hasDot = false;
                        newText = display.getText().toString().substring(start, end);
                        display.setText(newText);
                    }
                newText = display.getText().toString().substring(start, end);
                display.setText(newText);
            }
            if (display.getText().toString().length() == 0) {
                display.setText("0");
            }
        } catch (Exception ex) {
            display.setText("0");
        }
    }
}
