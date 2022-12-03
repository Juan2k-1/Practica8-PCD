/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import Controlador.Tienda;
import Vista.CanvasTienda;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juald
 */
public class Reparar implements Callable<Integer> {

    private int id;
    private CanvasTienda cv;
    private Tienda tienda;

    public Reparar(int id, CanvasTienda cv, Tienda tienda) {
        this.id = id;
        this.cv = cv;
        this.tienda = tienda;
    }

    @Override
    public Integer call() {
        try {
            System.out.println("Hilo repara: " + Thread.currentThread().getId() + " entra en la cola de espera de reparar la tarea: " + id + " para reparar");
            this.cv.inserta('R', this.id);
            System.out.println("Hilo repara: " + Thread.currentThread().getId() + " entra en tienda: " + id + " para reparar");
            this.tienda.EntraReparar(this.id);
            Thread.sleep(2000);
            this.tienda.SaleReparar(this.id);
            System.out.println("Hilo repara: " + Thread.currentThread().getId() + " ya estoy reparado, saliendo de la tienda");
        } catch (InterruptedException ex) {
            Logger.getLogger(Reparar.class.getName()).log(Level.SEVERE, null, ex);
        }

        return id;
    }
}
