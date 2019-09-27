package com.mydictionary.android;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.mydictionary.android.Model.Dictionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

	private RecyclerView recycler_disctionary;
	private DatabaseReference friendsDatabaseReference;
	private DatabaseReference userDatabaseReference;
	private FirebaseAuth mAuth;
	private String current_user_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		// TODO: 9/26/2019
		// Added By Peenalkumar
		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				/*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();*/
				Intent intent_createWord = new Intent(MainActivity.this,CreateToDo.class);
				Bundle bundle = new Bundle();
				bundle.putString("MODE","N");
				intent_createWord.putExtras(bundle);
				startActivity(intent_createWord);
			}
		});

		recycler_disctionary = (RecyclerView) findViewById(R.id.recycler_disctionary);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);

		linearLayoutManager.setReverseLayout(true);
		linearLayoutManager.setStackFromEnd(true);
		recycler_disctionary.setLayoutManager(linearLayoutManager);

		mAuth = FirebaseAuth.getInstance();
		current_user_id = mAuth.getCurrentUser().getUid();

		friendsDatabaseReference = FirebaseDatabase.getInstance().getReference().child("dictionary");
		userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
	}

	@Override
	protected void onStart() {
		super.onStart();
		FirebaseRecyclerOptions<Dictionary> recyclerOptions = new FirebaseRecyclerOptions.Builder<Dictionary>()
				.setQuery(friendsDatabaseReference, Dictionary.class)
				.build();

		FirebaseRecyclerAdapter<Dictionary, WordView> adapter = new FirebaseRecyclerAdapter<Dictionary, WordView>(recyclerOptions) {
			@Override
			protected void onBindViewHolder(@NonNull final WordView holder, int position, @NonNull Dictionary model) {
				final String userID = getRef(position).getKey();
				final String wordName = model.getWord_name();
				final String nounType = model.getN_type();
				final String orgin = model.getOrgin();
				final String ps_type = model.getPs_type();
				final String verb_str = model.getVerb();


				holder.word_name.setText(wordName);
				holder.txt_isNoun.setText(nounType);
				holder.txt_orgin_description.setText(orgin);
				holder.txt_isPlural.setText(" ( "+ps_type+" "+wordName+" )");
				holder.txt_verb_description.setText(verb_str);

				boolean isCompleteTask = true;
				if(orgin.equals(""))
				{
					isCompleteTask = false;
					holder.lbl_orgin.setVisibility(View.GONE);
					holder.txt_orgin_description.setVisibility(View.GONE);
				}
				else
				{
					holder.lbl_orgin.setVisibility(View.VISIBLE);
					holder.txt_orgin_description.setVisibility(View.VISIBLE);
				}

				if(verb_str.equals(""))
				{
					isCompleteTask = false;
					holder.lbl_verb.setVisibility(View.GONE);
					holder.txt_verb_description.setVisibility(View.GONE);
				}
				else
				{
					holder.lbl_verb.setVisibility(View.VISIBLE);
					holder.txt_verb_description.setVisibility(View.VISIBLE);
				}

				if(isCompleteTask)
					holder.img_isComplete.setImageResource(R.drawable.ic_complate);
				else
					holder.img_isComplete.setImageResource(R.drawable.ic_incompalete);

				holder.itemView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent_createWord = new Intent(MainActivity.this,CreateToDo.class);
						Bundle bundle = new Bundle();
						bundle.putParcelable("dictionary", model);
						bundle.putString("MODE","E");
						intent_createWord.putExtras(bundle);
						startActivity(intent_createWord);
					}
				});
				userDatabaseReference.child(userID).addValueEventListener(new ValueEventListener() {
					@Override
					public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
						if (dataSnapshot.exists()){

						}

					}

					@Override
					public void onCancelled(@NonNull DatabaseError databaseError) {

					}
				});

			}

			@NonNull
			@Override
			public WordView onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
				View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_word, viewGroup, false);
				return new WordView(view);
			}
		};

		recycler_disctionary.setAdapter(adapter);
		adapter.startListening();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_refresh) {
			onStart();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public static class WordView extends RecyclerView.ViewHolder{
		TextView word_name,txt_isNoun,txt_isPlural,lbl_verb,txt_verb_description,lbl_orgin,txt_orgin_description;
		//CircleImageView user_photo;
		ImageView img_isComplete;
		public WordView(View itemView) {
			super(itemView);
			word_name = itemView.findViewById(R.id.word_name);
			txt_isNoun = itemView.findViewById(R.id.txt_isNoun);
			txt_isPlural = itemView.findViewById(R.id.txt_isPlural);
			lbl_verb = itemView.findViewById(R.id.lbl_verb);
			txt_verb_description = itemView.findViewById(R.id.txt_verb_description);
			lbl_orgin = itemView.findViewById(R.id.lbl_orgin);
			txt_orgin_description = itemView.findViewById(R.id.txt_orgin_description);
			img_isComplete = itemView.findViewById(R.id.img_isComplete);

		}
	}
}
