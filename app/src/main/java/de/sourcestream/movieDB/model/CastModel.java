package de.sourcestream.movieDB.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Cast model class.
 * Used in Movie details -> Cast list
 */
public class CastModel implements Parcelable {
    private String name;
    private String character;
    private String profilePath;
    private int id;

    public CastModel() {
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    protected CastModel(Parcel in) {
        name = in.readString();
        character = in.readString();
        profilePath = in.readString();
        id = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(character);
        dest.writeString(profilePath);
        dest.writeInt(id);
    }

    public static final Parcelable.Creator<CastModel> CREATOR = new Parcelable.Creator<CastModel>() {
        @Override
        public CastModel createFromParcel(Parcel in) {
            return new CastModel(in);
        }

        @Override
        public CastModel[] newArray(int size) {
            return new CastModel[size];
        }
    };

}
