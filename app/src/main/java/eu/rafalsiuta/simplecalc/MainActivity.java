package eu.rafalsiuta.simplecalc;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import com.bumptech.glide.Glide;
import com.opencsv.CSVWriter;

import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import at.markushi.ui.CircleButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PERMISSION_REQUEST_STORAGE = 1000;
    private Button mainBtn;
    private TextView input, output, codeInput, codeOutput, rateTxt, timeTxt;
    private List<Currency> currencyList;
    private ImageView countryFlagFrom, countryFlagTo;
    private CircleButton historyBtn;
    Currency currency;
    RelativeLayout relativeLayoutIN, relativeLayoutOUT;
    String currentRate = null;
    private static final int hello = 11;
    private static final int CURRENCY_LIST_IN = 0;
    private static final int CURRENCY_LIST_OUT = 1;
    private final int[][] btnId = {{R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine, R.id.zero},                                                                   //case 2: special operator
            {R.id.swapBtn, R.id.clearBtn, R.id.deleteBtn, R.id.dot, R.id.equal}};

    private List<Button> btnList = new ArrayList<>();
    private List<History> historyList = new ArrayList<>();

    public static DecimalFormat nf = new DecimalFormat("###,###.#####");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        currencyList = new ArrayList<>();
        historyList = CSVFuncs.loadFile(this);

        output = findViewById(R.id.output);
        input = findViewById(R.id.input);

        codeInput = findViewById(R.id.codeInput);
        codeOutput = findViewById(R.id.codeOutput);

        countryFlagFrom = findViewById(R.id.countryFlagFrom);
        countryFlagTo = findViewById(R.id.countryFlagTo);

        relativeLayoutIN = (RelativeLayout) findViewById(R.id.linear1);
        relativeLayoutOUT = (RelativeLayout) findViewById(R.id.linear2);

        rateTxt = findViewById(R.id.rateTxt);
        timeTxt = findViewById(R.id.timeTxt);

        historyBtn = findViewById(R.id.historyBtn);
        new ProgressAsyncTask().execute();

        input.setSelected(true);
        finderId(mainBtn, btnId, btnList);

        relativeLayoutIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CurrencyActivity.class);
                startActivityForResult(intent, CURRENCY_LIST_IN);
            }
        });

        relativeLayoutOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CurrencyActivity.class);
                startActivityForResult(intent, CURRENCY_LIST_OUT);
            }
        });

        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CSVFuncs.saveData(MainActivity.this, historyList);
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CURRENCY_LIST_IN && resultCode == Activity.RESULT_OK) {
            currency = new Currency(data);
            if (!checkSameCode(CURRENCY_LIST_IN, currency)) {
                codeInput.setText(currency.getCurrencyCode());
                Glide.with(this).load(currency.getFlag()).into(countryFlagFrom);
            }
            new ProgressAsyncTask().execute();

        }

        if (requestCode == CURRENCY_LIST_OUT && resultCode == Activity.RESULT_OK) {
            currency = new Currency(data);
            if (!checkSameCode(CURRENCY_LIST_OUT, currency)) {
                codeOutput.setText(currency.getCurrencyCode());
                Glide.with(this).load(currency.getFlag()).into(countryFlagTo);
            }
            new ProgressAsyncTask().execute();
        }
    }
    @Override
    public void onRestart() {
        super.onRestart();
        historyList = CSVFuncs.loadFile(this);
    }
    public boolean checkSameCode(int status, Currency currency) {
        switch (status) {
            case 0:
                if (codeOutput.getText().toString().equals(currency.getCurrencyCode())) {
                    swapCurrency(currency, codeInput, codeOutput, countryFlagFrom, countryFlagTo);
                    return true;
                }
                return false;
            case 1:
                if (codeInput.getText().toString().equals(currency.getCurrencyCode())) {
                    swapCurrency(currency, codeOutput, codeInput, countryFlagTo, countryFlagFrom);
                    return true;
                }
                return false;
        }
        return false;
    }

    public void swapCurrency(Currency currency, TextView codeInput, TextView codeOutput, ImageView imageFrom, ImageView imageTo) {
        Glide.with(this).load(imageFrom.getDrawable()).into(imageTo);
        Glide.with(this).load(currency.getFlag()).into(imageFrom);
        String temp = codeInput.getText().toString();
        codeInput.setText(codeOutput.getText().toString());
        codeOutput.setText(temp);
    }


    private void finderId(Button btn, int[][] id, List<Button> list) {
        for (int[] idx : id) {
            for (int idy : idx) {
                btn = findViewById(idy);
                btn.setOnClickListener(this);
                list.add(btn);

            }
        }
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < btnId.length; i++) {
            switch (i) {
                case 0:
                    for (int j = 0; j < btnId[i].length; j++) {
                        if (v.getId() == btnId[i][j]) {
                            Logic.onNumbers(v, input, nf);

                        }
                    }
                    break;
                case 1:
                    for (int j = 0; j < btnId[i].length; j++) {
                        if (v.getId() == btnId[i][j]) {
                            if (j == 0) {
                                Logic.onSwap(this, codeInput, codeOutput, countryFlagFrom, countryFlagTo);
                                new ProgressAsyncTask().execute();
                            } else if (j == 1) {
                                Logic.onClear(input, output);
                            } else if (j == 2) {
                                Logic.onDelete(input, output);
                            } else if (j == 3) {
                                Logic.onDecimal(v, input);
                            } else if (j == 4) {
                                if (!"0".equals(input.getText().toString())) {
                                    Logic.onEqual(input, output, currentRate, nf);
                                    Date date = new Date();
                                    date = new Date(date.getTime());
                                    History newConversion = new History(codeInput.getText().toString(),
                                            codeOutput.getText().toString(),
                                            input.getText().toString(),
                                            output.getText().toString(),
                                            currentRate,
                                            new SimpleDateFormat("d/M/yy h:mm a").format(date));
                                    historyList.add(newConversion);
                                }
                            }
                        }
                    }
                    break;
            }

        }

    }



    private class ProgressAsyncTask extends AsyncTask<Void, Integer, String> {
        @Override
        protected String doInBackground(Void... voids) {

            String codeIn = codeInput.getText().toString().toLowerCase();
            String codeOut = codeOutput.getText().toString().toLowerCase();
            String desecription = null;
            try {
                String URL = "https://" + codeIn + ".fxexchangerate.com/" + codeOut + ".xml";

                URL url = new URL(URL);
                URLConnection conn = url.openConnection();

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(conn.getInputStream());

                NodeList nodes = doc.getElementsByTagName("item");
                Element element = (Element) nodes.item(0);
                NodeList title = element.getElementsByTagName("description");
                Element line = (Element) title.item(0);
                desecription = line.getTextContent();

            } catch (Exception e) {
                System.out.print(e);
            }
            return desecription;
        }

        @Override
        protected void onPostExecute(String result) {
            String fullRate = html2text(result);
            int splitPos = findWhiteSpace(fullRate, 5);
            fullRate = fullRate.substring(0, splitPos);
            rateTxt.setText(fullRate);
            Date date = new Date();
            date = new Date(date.getTime());
            timeTxt.setText(new SimpleDateFormat("d/M/yy h:mm a").format(date));
            currentRate = getRate(fullRate);
            output.setText("0");
            output.setTextSize(28.0F);

        }
    }

    public static String html2text(String html) {
        html = Jsoup.parse(html).text();
        html = html.replaceAll(" Converter -- Historical", "");
        return html;
    }

    public static String getRate(String fullRate) {
        int start = findWhiteSpace(fullRate, 3) + 1;
        int end = findWhiteSpace(fullRate, 4);

        fullRate = fullRate.substring(start, end);
        return fullRate;
    }

    public static int findWhiteSpace(String input, int position) {
        int spaceCount = 0;
        int splitPosition = -1;
        for (char c : input.toCharArray()) {
            splitPosition++;
            if (c == ' ') {
                spaceCount++;
                if (position == spaceCount)
                    break;
            }
        }
        return splitPosition;
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
        }
    }
}
