package br.android.jobregister.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import br.android.jobregister.model.Usuario;

/**
 private int id;
 private String nomeCompleto;
 private int codUsuario;
 private String senhaUsuario;
 private String CPF;
 private int tipo;
 private String cargo;
 private int genero;
 */

public class UsuarioDAO extends SQLiteOpenHelper{

    private final String SQL=
            "create table usuario(" +
                    "id integer primary key autoincrement," +
                    "nomeCompleto text not null," +
                    "codUsuario integer not null," +
                    "senhaUsuario text not null," +
                    "CPF text not null," +
                    "tipo integer not null," +
                    "cargo text not null," +
                    "genero integer not null);";

    private SQLiteDatabase db;
    private SQLiteStatement cmd;

    public UsuarioDAO(Context context) { super(context,"usuario.db", null, 1);}
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE usuario;");
        db.execSQL(SQL);
    }
    public long inserir(String nomeCompleto, int codUsuario, String senhaUsuario,
                        String CPF, int tipo, String cargo, int genero){
        this.db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("nomeCompleto",nomeCompleto);
        cv.put("codUsuario", codUsuario);
        cv.put("senhaUsuario", senhaUsuario);
        cv.put("CPF", CPF);
        cv.put("tipo", tipo);
        cv.put("cargo", cargo);
        cv.put("genero", genero);

        long id = this.db.insert("usuario", null, cv);
        return id;
    }

    public long atualizar(ContentValues usuario){
        this.db = getWritableDatabase();
        String where = "id = ?";
        String whereArgs[] = new String[]{usuario.getAsString("id")};
        return this.db.update("usuario", usuario, where, whereArgs);
    }

    public Usuario pesquisarPorId(String id){
        String sql = "SELECT * FROM usuario WHERE ID=?";
        String where[] = new String[]{id};

        //Definir permissão de leitura
        this.db = getReadableDatabase();

        //Realizar a consulta
        Cursor c = this.db.rawQuery(sql, where);

        if (c.moveToFirst()){
            Usuario p = new Usuario();
            p.setId(c.getInt(c.getColumnIndex("id")));
            p.setNomeCompleto(c.getString(c.getColumnIndex("nomeCompleto")));
            p.setCodUsuario(c.getInt(c.getColumnIndex("codUsuario")));
            p.setSenhaUsuario(c.getString(c.getColumnIndex("senhaUsuario")));
            p.setCPF(c.getString(c.getColumnIndex("CPF")));
            p.setTipo(c.getInt(c.getColumnIndex("tipo")));
            p.setCargo(c.getString(c.getColumnIndex("cargo")));
            p.setGenero(c.getInt(c.getColumnIndex("genero")));
            return p;
        }else{
            return null;
        }
    }

    public long remover(String id){
        this.db = getWritableDatabase();
        String where = "id = ?";
        String whereArgs[] = new String[]{id};
        return this.db.delete("usuario", where, whereArgs);
    }

    public List<Usuario> pesquisarPorUsuario(String nome){

        String sql = null;
        String where[] = null;
        if (nome.equals("")){
            sql = "SELECT * FROM usuario ORDER BY codUsuario";
        }else{
            sql = "SELECT * FROM usuario WHERE UPPER(nomeCompleto) like ? ORDER BY codUsuario";
            where = new String[]{"%" + nome.toUpperCase() + "%"};
        }

        this.db = getReadableDatabase();
        Cursor c = this.db.rawQuery(sql, where);

        List<Usuario> lista = new ArrayList<>();
        if (c.moveToFirst()){
            do {
                Usuario p = new Usuario();
                p.setId(c.getInt(c.getColumnIndex("id")));
                p.setNomeCompleto(c.getString(c.getColumnIndex("nomeCompleto")));
                p.setCodUsuario(c.getInt(c.getColumnIndex("codUsuario")));
                p.setSenhaUsuario(c.getString(c.getColumnIndex("senhaUsuario")));
                p.setCPF(c.getString(c.getColumnIndex("CPF")));
                p.setTipo(c.getInt(c.getColumnIndex("tipo")));
                p.setCargo(c.getString(c.getColumnIndex("cargo")));
                p.setGenero(c.getInt(c.getColumnIndex("genero")));
                lista.add(p);
            }while(c.moveToNext());
            return lista;
        }else{
            return null;
        }

    }

}
