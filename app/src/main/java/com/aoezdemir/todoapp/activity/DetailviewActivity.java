package com.aoezdemir.todoapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aoezdemir.todoapp.R;
import com.aoezdemir.todoapp.activity.adapter.ContactAdapter;
import com.aoezdemir.todoapp.crud.local.TodoDBHelper;
import com.aoezdemir.todoapp.model.Todo;

import java.util.Objects;


public class DetailviewActivity extends AppCompatActivity {

    public final static String INTENT_KEY_TODO = "DETAIL_KEY_TODO";

    private Todo todo;
    private TodoDBHelper db;
    ImageView IVPreviewImage;
    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        IVPreviewImage = findViewById(R.id.IVPreviewImage);
        todo = (Todo) getIntent().getSerializableExtra(INTENT_KEY_TODO);
        db = new TodoDBHelper(this);
        initializeDetailView();
    }

    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if (mAccel > 12) {
                boolean dbDeletionSucceeded = db.deleteTodo(todo.getId());
                if (dbDeletionSucceeded) {
                    finish();
                } else {
                }
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.iDelete) {
            new AlertDialog.Builder(this)
                    .setTitle("Confirm deletion")
                    .setMessage("Are you sure to delete this todo?")
                    .setCancelable(true)
                    .setNegativeButton("No", (DialogInterface dialog, int id) -> dialog.cancel())
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            boolean dbDeletionSucceeded = db.deleteTodo(todo.getId());
                            if (dbDeletionSucceeded) {
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Local error: Failed to deleteAllTodos todo.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .create()
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OverviewActivity.REQUEST_EDIT_TODO && resultCode == RESULT_OK &&
                data != null && data.hasExtra(EditActivity.INTENT_KEY_TODO)) {
            todo = (Todo) data.getSerializableExtra(EditActivity.INTENT_KEY_TODO);
            initializeDetailView();
        }
    }

    private void initializeDetailView() {
        loadTodoTitle();
        loadTodoDescription();
        loadTodoDate();
        loadTodoDoneIcon();
        loadTodoFavouriteIcon();
        loadTodoEdit();
        loadTodoContacts();
        loadImage();
        loadTodoPrice();
    }
    private void loadImage() {
        byte[] arrayByte = todo.getImageFromDb();
        if(arrayByte != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(arrayByte,0,arrayByte.length);
            IVPreviewImage.setVisibility(View.VISIBLE);
            IVPreviewImage.setImageBitmap(bitmap);
        }

    }

    private void loadTodoTitle() {
        TextView tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailTitle.setText(todo.getName());
        tvDetailTitle.setTextColor(getResources().getColor(R.color.colorTodoTitleDefault, null));
    }

    private void loadTodoDescription() {
        TextView tvDetailDescription = findViewById(R.id.tvDetailDescription);
        tvDetailDescription.setText(todo.getDescription());
        tvDetailDescription.setTextColor(getResources().getColor(R.color.colorTodoDescriptionDefault, null));
    }

    private void loadTodoPrice() {
        TextView tvDetailPrice = findViewById(R.id.tvDetailPrice);
        tvDetailPrice.setText(todo.getPriceWithCurr());
        tvDetailPrice.setTextColor(getResources().getColor(R.color.colorTodoDescriptionDefault, null));
    }

    private void loadTodoDate() {
        TextView tvDetailDate = findViewById(R.id.tvDetailDate);
        int textColor = todo.isDone() ? R.color.colorTodoDateDefault : todo.isExpired() ? R.color.colorTodoDateExpired : R.color.colorTodoDateDefault;
        tvDetailDate.setText(todo.formatExpiry());
        tvDetailDate.setTextColor(getResources().getColor(textColor, null));
        ((ImageView) findViewById(R.id.ivDetailDateIcon)).setImageDrawable(getResources().getDrawable(todo.isDone() ? R.drawable.ic_event_note_dark_gray_24dp : todo.isExpired() ? R.drawable.ic_event_note_red_24dp : R.drawable.ic_event_note_dark_gray_24dp, null));
    }

    private void loadTodoDoneIcon() {
        ImageView ibDetailDone = findViewById(R.id.ibDetailDone);
        ibDetailDone.setImageResource(todo.isDone() ? R.drawable.ic_check_circle_green_24dp : todo.isExpired() ? R.drawable.ic_error_outline_red_24dp : R.drawable.ic_radio_button_not_done_green_24dp);
    }

    private void loadTodoFavouriteIcon() {
        ImageView ibDetailFavourite = findViewById(R.id.ibDetailFavourite);
        ibDetailFavourite.setImageResource(todo.isFavourite() ? R.drawable.ic_favorite_red_24dp : R.drawable.ic_favorite_border_dark_gray_24dp);
    }

    private void loadTodoEdit() {
        findViewById(R.id.fbaEditTodo).setOnClickListener((View v) -> {
            Intent editIntent = new Intent(v.getContext(), EditActivity.class);
            editIntent.putExtra(EditActivity.INTENT_KEY_TODO, todo);
            editIntent.putExtra(RouterEmptyActivity.INTENT_IS_WEB_API_ACCESSIBLE, false);
            ((Activity) v.getContext()).startActivityForResult(editIntent, OverviewActivity.REQUEST_EDIT_TODO);
        });
    }

    private void loadTodoContacts() {
        RecyclerView rvContacts = findViewById(R.id.rvDetailContacts);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvContacts.setLayoutManager(linearLayoutManager);
        rvContacts.setAdapter(new ContactAdapter(todo, false, getContentResolver(), this));
        ((TextView) findViewById(R.id.tvDetailContacts)).setText(getResources().getString(todo.getContacts() == null || todo.getContacts().isEmpty() ?
                R.string.default_no_contacts_title :
                R.string.default_contacts_title));
    }
}