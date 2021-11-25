package fft

import chisel3._
import chisel3.util._
import chisel3.experimental._


class twiddleRegFile extends Module {
  val io = IO(new Bundle {
      val rdaddr = Output(UInt(3.W))
      val out_en = Output(UInt(1.W))
      val out = Output(UInt(32.W))
  })
  
  // just for testing purposes -- will input the real 8-pt FFT twiddle factors
  // note they will be complex numbers -- fixed point real and imaginary parts
  // rdaddr will be controlled by state machine or stage counter
  // will probably need a means of ready signal so that the value can be loaded to the BF.
  // maybe store in registers inside the BF module before the BF starts
  
  val reg0 = RegInit(1.U(32.W))
  val reg1 = RegInit(2.U(32.W))
  val reg2 = RegInit(3.U(32.W))
  val reg3 = RegInit(4.U(32.W))
  val reg4 = RegInit(5.U(32.W))
  val reg5 = RegInit(6.U(32.W))
  val reg6 = RegInit(7.U(32.W))
  val reg7 = RegInit(8.U(32.W))
  
  io.out_en := 0.U
  io.out := 0.U
  io.rdaddr := 0.U
  
  when((io.out_en).asBool){
    switch(io.rdaddr){
    	is(0.U){io.out := reg0}
    	is(1.U){io.out := reg1}
    	is(2.U){io.out := reg2}
    	is(3.U){io.out := reg3}
    	is(4.U){io.out := reg4}
    	is(5.U){io.out := reg5}
    	is(6.U){io.out := reg6}
    	is(7.U){io.out := reg7}
    }
  }
}
  
  
