package com.example.arthur.biblia_digital;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Arthur on 17/04/2015.
 */
public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "database.db";
    public static final String TABLE_FAVORITOS = "favoritos";
    public static final String TABLE_HISTORICO = "historico";
    public static final String TABLE_VERSICULO_DIARIO = "versiculo_diario";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LIVRO = "livro";
    public static final String COLUMN_CAPITULO = "capitulo";
    public static final String COLUMN_VERSICULO = "versiculo";
    public static final String COLUMN_ID_VERSICULO = "id_versiculo";
    public static final String COLUMN_ANO = "ano";
    public static final String COLUMN_DIA = "dia";

    public MyDBHandler(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*String CREATE_FAVORITOS_TABLE = "CREATE TABLE " +
                TABLE_FAVORITOS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_LIVRO
                + " TEXT," + COLUMN_CAPITULO + " INTEGER," + COLUMN_VERSICULO + " TEXT" + ")";*/
        String query = "CREATE TABLE favoritos (id INTEGER PRIMARY KEY AUTOINCREMENT, livro TEXT, capitulo INTEGER, versiculo TEXT, id_versiculo INTEGER)";
        db.execSQL(query);
        String query2 = "CREATE TABLE historico (id INTEGER PRIMARY KEY AUTOINCREMENT, livro TEXT, capitulo INTEGER)";
        db.execSQL(query2);
        String query3 = "CREATE TABLE versiculo_diario (id INTEGER PRIMARY KEY AUTOINCREMENT, ano INTEGER, dia INTEGER, livro TEXT, capitulo INTEGER, versiculo TEXT, id_versiculo INTEGER)";
        db.execSQL(query3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITOS);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORICO);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VERSICULO_DIARIO);
        onCreate(db);
    }


    public void adicionarHistorico(Historico historico) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_LIVRO, historico.getLivro());
        values.put(COLUMN_CAPITULO, historico.getCapitulo());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_HISTORICO, null, values);
        db.close();
    }


    public void adicionarVersiculoDiario(VersiculoDiario versiculo){
        ContentValues values = new ContentValues();
        values.put(COLUMN_LIVRO, versiculo.getLivro());
        values.put(COLUMN_CAPITULO, versiculo.getCapitulo());
        values.put(COLUMN_VERSICULO, versiculo.getVersiculo());
        values.put(COLUMN_ID_VERSICULO, versiculo.getId_versiculo());
        values.put(COLUMN_ANO, versiculo.getAno());
        values.put(COLUMN_DIA, versiculo.getDia());
        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_VERSICULO_DIARIO, null, values);
        db.close();
    }

    public VersiculoDiario ultimoVersiculoDiario(){
        String query = "Select * FROM " + TABLE_VERSICULO_DIARIO + " ORDER BY id DESC LIMIT 1";
        VersiculoDiario versiculo = new VersiculoDiario();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                versiculo.setId(Integer.parseInt(cursor.getString(0)));
                versiculo.setAno(cursor.getInt(1));
                versiculo.setDia(cursor.getInt(2));
                versiculo.setLivro(cursor.getString(3));
                versiculo.setCapitulo(Integer.parseInt(cursor.getString(4)));
                versiculo.setVersiculo(cursor.getString(5));
                versiculo.setId_versiculo(Integer.parseInt(cursor.getString(6)));
                cursor.moveToNext();
            }
        } else {
            //favorito = null;
        }
        cursor.close();
        db.close();
        return versiculo;
    }

    public boolean adicionarFavorito(Favorito favorito) {

        boolean aux = true;
        if (listaFavoritos().isEmpty()){
            ContentValues values = new ContentValues();
            values.put(COLUMN_LIVRO, favorito.getLivro());
            values.put(COLUMN_CAPITULO, favorito.getCapitulo());
            values.put(COLUMN_VERSICULO, favorito.getVersiculo());
            values.put(COLUMN_ID_VERSICULO, favorito.getId_versiculo());

            SQLiteDatabase db = this.getWritableDatabase();
            db.insert(TABLE_FAVORITOS, null, values);
            db.close();
            return true;
        } else {
            for (int i = 0; i < listaFavoritos().size(); i++) {
                Favorito favoritoAntigo = listaFavoritos().get(i);
                if (favorito.getLivro().equals(favoritoAntigo.getLivro()) && favorito.getCapitulo() == favoritoAntigo.getCapitulo()
                        && favorito.getId_versiculo() == favoritoAntigo.getId_versiculo()) {
                    System.out.println(favorito.getLivro() + " " + favoritoAntigo.getLivro() + " " + favorito.getCapitulo() + " " + favoritoAntigo.getCapitulo() + " " +
                    favorito.getId_versiculo() + " " + favoritoAntigo.getId_versiculo());
                    aux = false;
                }
            }
            System.out.println(aux);
            if (aux) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_LIVRO, favorito.getLivro());
                values.put(COLUMN_CAPITULO, favorito.getCapitulo());
                values.put(COLUMN_VERSICULO, favorito.getVersiculo());
                values.put(COLUMN_ID_VERSICULO, favorito.getId_versiculo());

                SQLiteDatabase db = this.getWritableDatabase();
                db.insert(TABLE_FAVORITOS, null, values);
                db.close();
                return true;
            }
        }
        return false;
    }

    public ArrayList<Favorito> listaFavoritos() {
        String query = "Select * FROM " + TABLE_FAVORITOS;
        ArrayList<Favorito> listaFavoritos = new ArrayList<Favorito>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                Favorito favorito = new Favorito();
                favorito.setId(Integer.parseInt(cursor.getString(0)));
                favorito.setLivro(cursor.getString(1));
                favorito.setCapitulo(Integer.parseInt(cursor.getString(2)));
                favorito.setVersiculo(cursor.getString(3));
                favorito.setId_versiculo(Integer.parseInt(cursor.getString(4)));
                listaFavoritos.add(favorito);
                cursor.moveToNext();
            }
        } else {
            //favorito = null;
        }
        cursor.close();
        db.close();
        return listaFavoritos;
    }

    public ArrayList<String> listaFavoritosLivros() {
        String query = "Select livro FROM " + TABLE_FAVORITOS;
        ArrayList<String> listaFavoritosLivros = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                if (!listaFavoritosLivros.contains(cursor.getString(0))) {
                    listaFavoritosLivros.add(cursor.getString(0));
                }
                cursor.moveToNext();
            }
        } else {
            //favorito = null;
        }
        cursor.close();
        db.close();
        return listaFavoritosLivros;
    }

    public ArrayList<Historico> listaHistorico() {
        String query = "Select * FROM " + TABLE_HISTORICO + " ORDER BY id DESC";
        ArrayList<Historico> listaHistorico = new ArrayList<Historico>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                Historico historico = new Historico();
                historico.setId(Integer.parseInt(cursor.getString(0)));
                historico.setLivro(cursor.getString(1));
                historico.setCapitulo(Integer.parseInt(cursor.getString(2)));
                listaHistorico.add(historico);
                cursor.moveToNext();
            }
        } else {
            //favorito = null;
        }
        cursor.close();
        db.close();
        return listaHistorico;
    }
    

    public boolean excluirFavorito(Favorito favorito) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITOS, COLUMN_ID + " = ?", new String[] { String.valueOf(favorito.getId()) });
        db.close();
        return true;
    }

    public void excluirFavoritos(ArrayList<Favorito> lista){
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < lista.size(); i++) {
            String query = "DELETE FROM " + TABLE_FAVORITOS + " WHERE id = " + lista.get(i).getId();
            db.execSQL(query);
        }
        db.close();
    }

    public Historico ultimoHistorico(){
        String query = "SELECT * FROM " + TABLE_HISTORICO + " ORDER BY id DESC LIMIT 1";
        Historico historico = new Historico();;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                historico.setId(Integer.parseInt(cursor.getString(0)));
                historico.setLivro(cursor.getString(1));
                historico.setCapitulo(Integer.parseInt(cursor.getString(2)));
                cursor.moveToNext();
            }
        } else {
            //favorito = null;
        }
        cursor.close();
        db.close();
        return historico;
    }

    public void limparHistorico(){
        String query = "DELETE FROM " + TABLE_HISTORICO;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

}
