package com.example.livedata;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import kotlin.jvm.functions.Function1;

public class EntrenadorViewModel extends AndroidViewModel {
    Entrenador entrenador;

    LiveData<Integer> ejercicioLiveData;
    LiveData<String> repeticionLiveData;

    public EntrenadorViewModel(@NonNull Application application) {
        super(application);

        entrenador = new Entrenador();

        MutableLiveData<String> liveDataRepeticion = new MutableLiveData<>();
        repeticionLiveData = liveDataRepeticion;

        ejercicioLiveData = Transformations.switchMap(entrenador.ordenLiveData, new Function1<String, LiveData<Integer>>() {

            String ejercicioAnterior;

            @Override
            public LiveData<Integer> invoke(String orden) {
                String ejercicio = orden.split(":")[0];

                if(!ejercicio.equals(ejercicioAnterior)){
                    ejercicioAnterior = ejercicio;
                    int imagen;
                    switch (ejercicio) {
                        case "EJERCICIO1":
                        default:
                            imagen = R.drawable.e1;
                            break;
                        case "EJERCICIO2":
                            imagen = R.drawable.e2;
                            break;
                        case "EJERCICIO3":
                            imagen = R.drawable.e3;
                            break;
                        case "EJERCICIO4":
                            imagen = R.drawable.e4;
                            break;
                    }

                    return new MutableLiveData<>(imagen);
                }
                return null;
            }

        });

        entrenador.ordenLiveData.observeForever(orden -> {
            if (orden != null && orden.contains(":")) {
                String repeticion = orden.split(":")[1];
                liveDataRepeticion.setValue(repeticion);
            } else {
                liveDataRepeticion.setValue("Invalid Format"); // Handle edge cases
            }
        });
    }

    LiveData<Integer> obtenerEjercicio(){
        return ejercicioLiveData;
    }

    LiveData<String> obtenerRepeticion(){
        return repeticionLiveData;
    }
}