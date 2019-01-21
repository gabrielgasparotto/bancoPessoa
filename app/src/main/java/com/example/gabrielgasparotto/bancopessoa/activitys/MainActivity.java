package com.example.gabrielgasparotto.bancopessoa.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.gabrielgasparotto.bancopessoa.R;
import com.example.gabrielgasparotto.bancopessoa.dao.ContaCorrenteApi;
import com.example.gabrielgasparotto.bancopessoa.fragments.ContaCorrenteFragment;
import com.example.gabrielgasparotto.bancopessoa.fragments.InstituicaoFragment;
import com.example.gabrielgasparotto.bancopessoa.fragments.TipoContaFragment;
import com.example.gabrielgasparotto.bancopessoa.model.ContaCorrente;
import com.example.gabrielgasparotto.bancopessoa.model.Instituicao;

import java.util.Random;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private char opcao;
    public static String textoPesquisado;

    private boolean loadFragment(Fragment fragment){
        if (fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.fragmentPrincipal, fragment)
                    .commit();
            return true;
        }

        return false;
    }

    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.navContaCorrente:
                        fragment = new ContaCorrenteFragment();
                        opcao = 'c';
                    break;
                case R.id.navTipoConta:
                    fragment = new TipoContaFragment();
                    opcao = 't';
                    break;
                case R.id.navInstituicao:
                    fragment = new InstituicaoFragment();
                    opcao = 'i';
                    break;
            }
            return loadFragment(fragment);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navContaCorrente);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FabSpeedDial fab = findViewById(R.id.fastSpeedDial);
        fab.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                Intent intent;
                if (menuItem.getTitle().equals("Conta Corrente")) {
                    intent = new Intent(MainActivity.this, ContaCorrenteActivity.class);
                    ContaCorrenteFragment.contaCorrente = null;
                    startActivity(intent);
                } else if (menuItem.getTitle().equals("Instituição")) {
                    intent = new Intent(MainActivity.this, InstituicaoActivity.class);
                    InstituicaoFragment.instituicaoGeral = null;
                    startActivity(intent);
                } else if (menuItem.getTitle().equals("Tipo Conta")) {
                    intent = new Intent(MainActivity.this, TipoContaActivity.class);
                    TipoContaFragment.tipoConta = null;
                    startActivity(intent);
                }
                return true;
            }

            @Override
            public void onMenuClosed() {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                textoPesquisado = s;
                switch (opcao) {
                    case 'c':
                        loadFragment(new ContaCorrenteFragment());
                        break;
                    case 'i':
                        loadFragment(new InstituicaoFragment());
                        break;
                    case 't':
                        loadFragment(new TipoContaFragment());
                        break;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if((s.isEmpty() || s.equals("") || s == null) && opcao == 'c'){
                    textoPesquisado = null;
                    loadFragment(new ContaCorrenteFragment());
                }else if((s.isEmpty() || s.equals("") || s == null) && opcao == 'i'){
                    textoPesquisado = null;
                    loadFragment(new InstituicaoFragment());
                }else if((s.isEmpty() || s.equals("") || s == null) && opcao == 't'){
                    textoPesquisado = null;
                    loadFragment(new TipoContaFragment());
                }
                return false;
            }
        });
        return true;
    }

}
