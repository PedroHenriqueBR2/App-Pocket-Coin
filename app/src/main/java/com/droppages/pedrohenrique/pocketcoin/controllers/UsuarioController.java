package com.droppages.pedrohenrique.pocketcoin.controllers;

import android.content.SharedPreferences;
import android.util.Log;

import com.droppages.pedrohenrique.pocketcoin.dal.Sessao;
import com.droppages.pedrohenrique.pocketcoin.exceptions.DadoInvalidoNoCadastroDeUsuarioException;
import com.droppages.pedrohenrique.pocketcoin.model.Configuracao;
import com.droppages.pedrohenrique.pocketcoin.model.NaturezaDaAcao;
import com.droppages.pedrohenrique.pocketcoin.model.Usuario;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;

public class UsuarioController {
    private Sessao              sessao;
    private Box<Usuario>        usuarioBox;
    private Box<Configuracao>   configuracaoBox;
    private Box<NaturezaDaAcao> naturezaBox;


    // Instancia com sessao
    public UsuarioController(BoxStore boxStore, SharedPreferences preferences) {
        this.configuracaoBox    = boxStore.boxFor(Configuracao.class);
        this.naturezaBox        = boxStore.boxFor(NaturezaDaAcao.class);
        this.usuarioBox         = boxStore.boxFor(Usuario.class);
        this.sessao             = new Sessao(preferences);
    }

    // Instancia sem sessao
    public UsuarioController(BoxStore boxStore) {
        this.configuracaoBox    = boxStore.boxFor(Configuracao.class);
        this.naturezaBox        = boxStore.boxFor(NaturezaDaAcao.class);
        this.usuarioBox         = boxStore.boxFor(Usuario.class);
    }

    public void cadastrarNovoUsuario(String nome, String login, String senha, String repeteSenha) throws DadoInvalidoNoCadastroDeUsuarioException {
        if (dadosValidosParaCadastro(nome, login, senha, repeteSenha)){
            Usuario usuario = new Usuario(nome, login, senha);
            usuarioBox.put(usuario);
        }
    }

    public boolean login(String login, String senha) throws DadoInvalidoNoCadastroDeUsuarioException {
        if (dadosValidosParaLogin(login, senha)){
            try {
                Usuario usuarioLocalizado = usuarioExistente(login, senha);
                sessao.adicionarDadosASessao(Sessao.USUARIO_ID, ""+usuarioLocalizado.id);
                sessao.adicionarDadosASessao(Sessao.USUARIO_NOME, ""+usuarioLocalizado.getNome());
                sessao.adicionarDadosASessao(Sessao.USUARIO_LOGIN, ""+usuarioLocalizado.getLogin());
                sessao.adicionarDadosASessao(Sessao.USUARIO_SENHA, ""+usuarioLocalizado.getSenha());
                return true;
            } catch (NullPointerException e) {
                throw new DadoInvalidoNoCadastroDeUsuarioException("Usuário não localizado");
            } catch (Exception e) {
                Log.e("loginUsuario", e.getMessage());
            }
        }
        return false;
    }

    public List<String> pegarDadosDoUsuarioLogado(){
        List<String> retorno = new ArrayList<>();
        retorno.add(sessao.recuperarDadosDaSessao(Sessao.USUARIO_ID));
        retorno.add(sessao.recuperarDadosDaSessao(Sessao.USUARIO_NOME));
        retorno.add(sessao.recuperarDadosDaSessao(Sessao.USUARIO_LOGIN));
        retorno.add(sessao.recuperarDadosDaSessao(Sessao.USUARIO_SENHA));
        return retorno;
    }

    private boolean dadosValidosParaCadastro(String nome, String login, String senha, String repeteSenha) throws DadoInvalidoNoCadastroDeUsuarioException {
        if (nome.length() == 0){
            throw new DadoInvalidoNoCadastroDeUsuarioException("Preencha o campo nome");
        } else if (login.length() == 0){
            throw new DadoInvalidoNoCadastroDeUsuarioException("Preencha o campo login");
        } else if (senha.length() == 0){
            throw new DadoInvalidoNoCadastroDeUsuarioException("Preencha o campo senha");
        } else if (repeteSenha.length() == 0){
            throw new DadoInvalidoNoCadastroDeUsuarioException("Preencha o campo repetir senha");
        } else {
            if (!senha.equals(repeteSenha)){
                throw new DadoInvalidoNoCadastroDeUsuarioException("Senhas diferentes, tente novamente");
            }
        }
        return true;
    }

    private boolean dadosValidosParaLogin(String login, String senha) throws DadoInvalidoNoCadastroDeUsuarioException {
        if (login.length() == 0){
            throw new DadoInvalidoNoCadastroDeUsuarioException("Preencha o campo login");
        } else if (senha.length() == 0){
            throw new DadoInvalidoNoCadastroDeUsuarioException("Preencha o campo senha");
        }
        return true;
    }

    private Usuario usuarioExistente(String login, String senha){
        for (Usuario usuario: usuarioBox.getAll()){
            if (usuario.getLogin().equals(login) && usuario.getSenha().equals(senha)){
                return usuario;
            }
        }
        throw new NullPointerException();
    }

    public void primeiraInstalacaoDoApp() {
        List<Configuracao> configuracoes = configuracaoBox.getAll();
        if (configuracoes.size() == 0) {
            configuracaoBox.put(new Configuracao("Instalado", "1"));
            naturezaBox.put(new NaturezaDaAcao("Crédito"));
            naturezaBox.put(new NaturezaDaAcao("Débito"));
        }
    }
}
