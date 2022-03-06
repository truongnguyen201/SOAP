package topica.edu.vn.soap;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.HttpURLConnection;

import topica.edu.vn.config.Configuration;

public class MainActivity extends AppCompatActivity {
    EditText txtC;
    Button btnF;
    TextView txtF;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvent();
    }

    private void addEvent() {
        btnF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLiDoF();
            }
        });
    }

    private void xuLiDoF() {
        ctoFTask task=new ctoFTask();
        task.execute(txtC.getText().toString());
    }
    class ctoFTask extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            txtF.setText("");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            txtF.setText(s);
            progressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String c=strings[0];
                //Tạo 1 yêu cầu để gửi lên server:
                SoapObject request=new SoapObject(Configuration.NAME_SPACE,Configuration.METHOD_C_TO_F);
                //Nếu yêu cầu này có đối số (parameter):
                //dua doi tuong len server
                request.addProperty(Configuration.PARAM_C_TO_F_CECIUS,c);
                //tạo Envelope
                //lớp  như bao bì vna chuyển evenlop
                SoapSerializationEnvelope envelope=
                        new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet=true;
                envelope.setOutputSoapObject(request);

                //tạo loại kết nối lên server
                HttpTransportSE httpTransportSE=new HttpTransportSE(Configuration.SERVER_URL);
                //dua di xu li gui key qua tra ve
                // gọi lệnh thực hiện hàm (hàm này đã thực sự xử lý trên Server rồi):
                httpTransportSE.call(Configuration.SOAP_ACTION_C_TO_F,envelope);

                //vì kết quả trả về theo mô tả là kiểu chuỗi
                // (hàm này là lấy kết quả sau khi xử lý):


                //dữ liệu đơn (dùng SoapPrimitive)
                SoapPrimitive data= (SoapPrimitive) envelope.getResponse();
                return data.toString();

            }
            catch (Exception ex)
            {
                Log.e("LOI",ex.toString());
            }
            return null;
        }
    }

    private void addControls() {
        txtC=findViewById(R.id.txtDoC);
        txtF=findViewById(R.id.txtKetQua);
        btnF=findViewById(R.id.btnDoF);
        progressDialog= new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("thong Báo");
        progressDialog.setMessage("Đang xử lí vui long cho");

    }
}