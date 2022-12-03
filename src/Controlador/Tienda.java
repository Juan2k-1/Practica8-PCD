/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Vista.CanvasTienda;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author juald
 */
public class Tienda {

    boolean VendedorLibre = true;
    boolean TecnicoLibre = true;
    private CanvasTienda cv;

    private Lock mutex;
    private Condition compradores;
    private Condition reparadores;

    int ncompradores = 0;

    public Tienda(CanvasTienda cv) {
        this.cv = cv;
        this.mutex = new ReentrantLock();
        this.compradores = this.mutex.newCondition();
        this.reparadores = this.mutex.newCondition();
    }

    public void EntraReparar(int id) throws InterruptedException {
        this.mutex.lock();
        try {
            while (!this.TecnicoLibre || this.ncompradores > 2) {
                this.reparadores.await();
            }
            this.TecnicoLibre = false;
            this.cv.quita('R', id);
            this.cv.repara('R', id);
        } finally {
            this.mutex.unlock();
        }
    }

    public void SaleReparar(int id) {
        this.mutex.lock();
        try {
            this.TecnicoLibre = true;
            this.cv.finalizado('M', id);
            this.reparadores.signal();
        } finally {
            this.mutex.unlock();
        }
    }

    public char EntraComprar(int id) throws InterruptedException {
        this.mutex.lock();
        char Quien = 0;
        try {
            while (!this.VendedorLibre && !this.TecnicoLibre) {
                this.ncompradores++;
                this.compradores.await();
                this.ncompradores--;
            }
            if (this.VendedorLibre) {
                this.VendedorLibre = false;
                Quien = 'C';
                this.cv.quita('C', id);
                this.cv.compra('V', id);
            } else if (this.TecnicoLibre) {
                this.TecnicoLibre = false;
                Quien = 'M';
                this.cv.quita('C', id);
                this.cv.compra('M', id);
            }           
        } finally {
            this.mutex.unlock();
        }
        return Quien;
    }

    public void SaleComprar(char tipo, int id) {
        this.mutex.lock();
        try {
            if (tipo == 'C') {
                this.VendedorLibre = true;
                this.cv.finalizado('V', id);
            } else if (tipo == 'M') {
                this.TecnicoLibre = true;
                this.cv.finalizado('M', id);
            }
        } finally {
            //System.out.println("Valor de ncompradores: " + this.ncompradores);
            if (this.ncompradores <= 2) {
                this.reparadores.signal();
                this.compradores.signal();   
            }
            else
                this.compradores.signal();           
            this.mutex.unlock();
        }
    }
}
