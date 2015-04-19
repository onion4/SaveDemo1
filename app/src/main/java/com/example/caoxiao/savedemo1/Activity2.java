package com.example.caoxiao.savedemo1;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

/**
 * Created by caoxiao on 15/4/19.
 */
public class Activity2 extends Activity {
    private ProgressBar progressBar=null;
    // 异步任务类
    private AsyncTaskTest myAsyncTask=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout2);

        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        // 获取对象
        myAsyncTask=(AsyncTaskTest)getLastNonConfigurationInstance();

        if (myAsyncTask==null) {
            myAsyncTask=new AsyncTaskTest(this);
            myAsyncTask.execute();
        }else {
            myAsyncTask.attach(this);
            updateProgress(myAsyncTask.getProgress());

            if (myAsyncTask.getProgress()>=100) {
                markAsDone();
            }
        }
    }

    /**
     * 保存对象
     */
    @Override
    public Object onRetainNonConfigurationInstance() {
        myAsyncTask.detach();

        return myAsyncTask;
    }

    private void updateProgress(int progress) {
        progressBar.setProgress(progress);
    }

    private void markAsDone() {
        findViewById(R.id.completed).setVisibility(View.VISIBLE);
    }

    // 异步任务类
    private static class AsyncTaskTest extends AsyncTask<Void, Void, Void> {
        private Activity2 activity=null;
        private int progress=0;

        /**
         * 默认的构造器
         */
        public AsyncTaskTest() {
            // TODO Auto-generated constructor stub
        }

        /**
         * 带参构造器
         * @param activity
         */
        public AsyncTaskTest(Activity2 activity) {
            attach(activity);
        }

        @Override
        protected Void doInBackground(Void... unused) {
            for (int i=0;i<20;i++) {
                SystemClock.sleep(500);
                publishProgress();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... unused) {
            if (activity==null) {
                Log.w("RotationAsyncActivity", "onProgressUpdate()");
            }else {
                progress += 5;
                activity.updateProgress(progress);
            }
        }

        @Override
        protected void onPostExecute(Void unused) {
            if (activity==null) {
                Log.w("RotationAsyncActivity", "onPostExecute()");
            }else {
                activity.markAsDone();
            }
        }

        protected void detach() {
            activity = null;
        }

        protected void attach(Activity2 activity) {
            this.activity = activity;
        }

        protected int getProgress() {
            return progress;
        }
    }
}
