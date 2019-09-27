package com.mydictionary.android.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Dictionary implements Parcelable {

	private String word_id = "";
	private String user_id = "";
	private String word_name = "";
	private String n_type = "";
	private String verb = "";
	private String orgin = "";
	private String ps_type = "";
	private long created_at ;
	private long update_at ;
	public Dictionary() {
	}
	public Dictionary(String word_id, String user_id, String word_name, String n_type, String verb, String orgin, String ps_type, long created_at, long update_at) {
		this.word_id = word_id;
		this.user_id = user_id;
		this.word_name = word_name;
		this.n_type = n_type;
		this.verb = verb;
		this.orgin = orgin;
		this.ps_type = ps_type;
		this.created_at = created_at;
		this.update_at = update_at;
	}
	public Dictionary(Parcel in)
	{
		this.word_id = in.readString();
		this.user_id = in.readString();
		this.word_name = in.readString();
		this.n_type = in.readString();
		this.verb = in.readString();
		this.orgin = in.readString();
		this.ps_type = in.readString();
		this.created_at = in.readLong();
		this.update_at = in.readLong();
	}

	public String getWord_id() {
		return word_id;
	}

	public void setWord_id(String word_id) {
		this.word_id = word_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getWord_name() {
		return word_name;
	}

	public void setWord_name(String word_name) {
		this.word_name = word_name;
	}

	public String getN_type() {
		return n_type;
	}

	public void setN_type(String n_type) {
		this.n_type = n_type;
	}

	public String getVerb() {
		return verb;
	}

	public void setVerb(String verb) {
		this.verb = verb;
	}

	public String getOrgin() {
		return orgin;
	}

	public void setOrgin(String orgin) {
		this.orgin = orgin;
	}

	public String getPs_type() {
		return ps_type;
	}

	public void setPs_type(String ps_type) {
		this.ps_type = ps_type;
	}

	public long getCreated_at() {
		return created_at;
	}

	public void setCreated_at(long created_at) {
		this.created_at = created_at;
	}

	public long getUpdate_at() {
		return update_at;
	}

	public void setUpdate_at(long update_at) {
		this.update_at = update_at;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {

		parcel.writeString(word_id);
		parcel.writeString(user_id);
		parcel.writeString(word_name);
		parcel.writeString(n_type);
		parcel.writeString(verb);
		parcel.writeString(orgin);
		parcel.writeString(ps_type);
		parcel.writeLong(created_at);
		parcel.writeLong(update_at);

	}

	public static final Parcelable.Creator<Dictionary> CREATOR = new Parcelable.Creator<Dictionary>()
	{
		public Dictionary createFromParcel(Parcel in)
		{
			return new Dictionary(in);
		}
		public Dictionary[] newArray(int size)
		{
			return new Dictionary[size];
		}
	};
}
