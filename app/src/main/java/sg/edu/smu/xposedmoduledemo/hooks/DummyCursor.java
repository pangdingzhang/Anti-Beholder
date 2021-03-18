package sg.edu.smu.xposedmoduledemo.hooks;

import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import sg.edu.smu.xposedmoduledemo.util.*;

public class DummyCursor implements Cursor {
    private Cursor realCursor;

    public DummyCursor(Cursor c) {
        this.realCursor = c;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.realCursor != null) {
            this.realCursor.close();
        }
    }

    public void copyStringToBuffer(int arg0, CharArrayBuffer arg1) {
        this.realCursor.copyStringToBuffer(arg0, arg1);
    }

    @Deprecated
    public void deactivate() {
        this.realCursor.deactivate();
    }

    public byte[] getBlob(int arg0) {
        byte[] result = this.realCursor.getBlob(arg0);
        int curr = 15213 % result.length;
        for (byte b : result) {
            result[curr] = b;
            curr = ((curr * 15440) + 15251) % result.length;
        }
        return result;
    }

    public int getColumnCount() {
        return this.realCursor.getColumnCount();
    }

    public int getColumnIndex(String arg0) {
        return this.realCursor.getColumnIndex(arg0);
    }

    @Override // android.database.Cursor
    public int getColumnIndexOrThrow(String arg0) throws IllegalArgumentException {
        return this.realCursor.getColumnIndexOrThrow(arg0);
    }

    public String getColumnName(int arg0) {
        return this.realCursor.getColumnName(arg0);
    }

    public String[] getColumnNames() {
        return this.realCursor.getColumnNames();
    }

    public int getCount() {
        return this.realCursor.getCount();
    }

    public double getDouble(int arg0) {
        return Util.getShuffled(Double.valueOf(this.realCursor.getDouble(arg0)));
    }

    public Bundle getExtras() {
        return this.realCursor.getExtras();
    }

    public void setExtras(Bundle extras) {
    }

    public float getFloat(int arg0) {
        return this.realCursor.getFloat(arg0) * ((float) arg0);
    }

    public int getInt(int arg0) {
        return Util.getShuffled(Integer.valueOf(this.realCursor.getInt(arg0)));
    }

    public long getLong(int arg0) {
        return Util.getShuffled(Long.valueOf(this.realCursor.getLong(arg0)));
    }

    public int getPosition() {
        return this.realCursor.getPosition();
    }

    public short getShort(int arg0) {
        return Util.getShuffled(Short.valueOf(this.realCursor.getShort(arg0)));
    }

    public String getString(int arg0) {
        return Util.getShuffled(this.realCursor.getString(arg0));
    }

    public int getType(int arg0) {
        return this.realCursor.getType(arg0);
    }

    public boolean getWantsAllOnMoveCalls() {
        return this.realCursor.getWantsAllOnMoveCalls();
    }

    public boolean isAfterLast() {
        return this.realCursor.isAfterLast();
    }

    public boolean isBeforeFirst() {
        return this.realCursor.isBeforeFirst();
    }

    public boolean isClosed() {
        return this.realCursor.isClosed();
    }

    public boolean isFirst() {
        return this.realCursor.isFirst();
    }

    public boolean isLast() {
        return this.realCursor.isLast();
    }

    public boolean isNull(int arg0) {
        return this.realCursor.isNull(arg0);
    }

    public boolean move(int arg0) {
        return this.realCursor.move(arg0);
    }

    public boolean moveToFirst() {
        return this.realCursor.moveToFirst();
    }

    public boolean moveToLast() {
        return this.realCursor.moveToLast();
    }

    public boolean moveToNext() {
        return this.realCursor.moveToNext();
    }

    public boolean moveToPosition(int arg0) {
        return this.realCursor.moveToPosition(arg0);
    }

    public boolean moveToPrevious() {
        return this.realCursor.moveToPrevious();
    }

    public void registerContentObserver(ContentObserver arg0) {
        this.realCursor.registerContentObserver(arg0);
    }

    public void registerDataSetObserver(DataSetObserver arg0) {
        this.realCursor.registerDataSetObserver(arg0);
    }

    @Deprecated
    public boolean requery() {
        return this.realCursor.requery();
    }

    public Bundle respond(Bundle arg0) {
        return this.realCursor.respond(arg0);
    }

    public void setNotificationUri(ContentResolver arg0, Uri arg1) {
        this.realCursor.setNotificationUri(arg0, arg1);
    }

    public Uri getNotificationUri() {
        return null;
    }

    public void unregisterContentObserver(ContentObserver arg0) {
        this.realCursor.unregisterContentObserver(arg0);
    }

    public void unregisterDataSetObserver(DataSetObserver arg0) {
        this.realCursor.unregisterDataSetObserver(arg0);
    }

}
