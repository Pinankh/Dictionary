package com.mydictionary.android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.mydictionary.android.Model.Dictionary;

import java.util.Date;
import java.util.HashMap;

import xyz.hasnat.sweettoast.SweetToast;

public class CreateToDo extends AppCompatActivity {

	private static final String TAG = "CreateToDo";
	private Context myContext = CreateToDo.this;

	private EditText
			word_name,
			verb,
			orgin;
	private AppCompatButton submit;
	private RadioGroup
			RG_nType,
			RG_psType;
	private RadioButton
			RB_Noun,
			RB_Pro_Noun,
			RB_Plural,
			RB_Singular;

	//Firebase
	private FirebaseAuth mAuth;
	private FirebaseUser user;

	private DatabaseReference storeDefaultDatabaseReference;

	private ProgressDialog progressDialog;
	private String MODE ="";
	private Dictionary PASSED_DICTIONARY;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_to_do);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);


		// TODO: 9/26/2019
		// Wegiet Declaration By PeenalKumar
		mAuth = FirebaseAuth.getInstance();
		user = mAuth.getCurrentUser();

		word_name = (EditText)findViewById(R.id.word_name);
		verb = (EditText)findViewById(R.id.verb);
		orgin = (EditText)findViewById(R.id.orgin);

		RG_nType = (RadioGroup) findViewById(R.id.RG_nType);
		RG_psType = (RadioGroup) findViewById(R.id.RG_psType);

		RB_Noun = (RadioButton) findViewById(R.id.RB_Noun);
		RB_Pro_Noun = (RadioButton) findViewById(R.id.RB_Pro_Noun);
		RB_Plural = (RadioButton) findViewById(R.id.RB_Plural);
		RB_Singular = (RadioButton) findViewById(R.id.RB_Singular);

		submit = (AppCompatButton) findViewById(R.id.submit);
		/// Declaration END

		// TODO: 9/26/2019
		// Declare Listener
		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(idDataValidate()) {
					if(MODE.equals("N"))
						AddEditDictionaryWord();
					else
						UpdateData();
				}
			}
		});
		RG_nType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int i) {

			}
		});
		RG_psType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int i) {

			}
		});
		// END Listener Declaration

		// TODO: 9/27/2019
		//Added by Pinalkumar This is get the data from Backgournd Activity Passed
		Bundle bundle = getIntent().getExtras();

		MODE = bundle.getString("MODE");

		if(MODE.equals("E"))
		{
			PASSED_DICTIONARY = (Dictionary) bundle.getParcelable("dictionary");
			word_name.setText(PASSED_DICTIONARY.getWord_name());
			verb.setText(PASSED_DICTIONARY.getVerb());
			orgin.setText(PASSED_DICTIONARY.getOrgin());

			if(PASSED_DICTIONARY.getN_type().equalsIgnoreCase("noun"))
			{
				RB_Noun.setChecked(true);
			}
			else
			{
				RB_Pro_Noun.setChecked(true);
			}

			if(PASSED_DICTIONARY.getPs_type().equalsIgnoreCase("singular"))
			{
				RB_Singular.setChecked(true);
			}
			else
			{
				RB_Plural.setChecked(true);
			}
		}

		progressDialog = new ProgressDialog(myContext);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
					}
		return true;
	}

	// TODO: 9/27/2019
	// Validation Field Method crated by peenalkumar
	private boolean idDataValidate()
	{
		boolean isValid = true;

		if(word_name.getText().toString().equals(""))
		{
			isValid = false;
			word_name.setError("Enter Word!");
			word_name.requestFocus();
		}
		else
		{
			word_name.setError(null);
		}


		return isValid;
	}


	private void AddEditDictionaryWord()
	{
		String str_nType = "";
		if(RB_Noun.isChecked())
		{
			str_nType = "Noun";
		}
		else
		{
			str_nType = "Pro Noun";
		}
		String str_psType = "";
		if(RB_Plural.isChecked())
		{
			str_psType = "Plural";
		}
		else
		{
			str_psType = "Singular";
		}

		String createID = user.getUid()+"_"+new Date().getTime();
		storeDefaultDatabaseReference = FirebaseDatabase.getInstance().getReference().child("dictionary").child(createID);
		storeDefaultDatabaseReference.child("word_id").setValue(createID);
		storeDefaultDatabaseReference.child("user_id").setValue(user.getUid());
		storeDefaultDatabaseReference.child("word_name").setValue(word_name.getText().toString());
		storeDefaultDatabaseReference.child("n_type").setValue(str_nType);
		storeDefaultDatabaseReference.child("verb").setValue(verb.getText().toString());
		storeDefaultDatabaseReference.child("orgin").setValue(orgin.getText().toString());
		storeDefaultDatabaseReference.child("ps_type").setValue(str_psType);
		storeDefaultDatabaseReference.child("created_at").setValue(ServerValue.TIMESTAMP);
		storeDefaultDatabaseReference.child("update_at").setValue(ServerValue.TIMESTAMP).addOnCompleteListener(new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task) {

				if(task.isSuccessful())
				{
					SweetToast.success(myContext,"Submit Successfully");
					finish();
				}
				else
				{
					String message = task.getException().getMessage();
					SweetToast.error(myContext, "Error occurred : " + message);
				}
			}
		});
	}

	// TODO: 9/27/2019
	private void UpdateData()
	{
		String str_nType = "";
		if(RB_Noun.isChecked())
		{
			str_nType = "Noun";
		}
		else
		{
			str_nType = "Pro Noun";
		}
		String str_psType = "";
		if(RB_Plural.isChecked())
		{
			str_psType = "Plural";
		}
		else
		{
			str_psType = "Singular";
		}

		HashMap<String, Object> message_text_body = new HashMap<>();
		message_text_body.put("word_name", word_name.getText().toString());
		message_text_body.put("n_type", str_nType);
		message_text_body.put("verb", verb.getText().toString());
		message_text_body.put("update_at", ServerValue.TIMESTAMP);
		message_text_body.put("orgin", orgin.getText().toString());
		message_text_body.put("ps_type", str_psType);
		storeDefaultDatabaseReference = FirebaseDatabase.getInstance().getReference().child("dictionary").child(PASSED_DICTIONARY.getWord_id());
		storeDefaultDatabaseReference.updateChildren(message_text_body, new DatabaseReference.CompletionListener() {
			@Override
			public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
				if (databaseError != null){
					Log.e("from_dictionary: ", databaseError.getMessage());
					SweetToast.error(myContext,databaseError.getMessage());
				}
				else
				{
					SweetToast.success(myContext,"Update Successfully");
					finish();
				}
			}
		});
	}
}
