package fft

import chisel3._
import chisel3.util._

class BF_block extends Module {
  val io = IO(new Bundle {
    val in = Input(Vec(8, SInt(32.W)))
    val out = Output(Vec(8, SInt(32.W)))
    val wn = Input(SInt(32.W))
    val stage_counter = Input(UInt(2.W))
    val out_en = Input(UInt(1.W))
  })
    
    for (i <- 0 until 8){
      io.out(i) := 0.S
    }
    
    val bf0 = Module(new Butterfly())
    val bf1 = Module(new Butterfly())
    val bf2 = Module(new Butterfly())
    val bf3 = Module(new Butterfly())
    val twid = Module(new Twiddle())
    twid.io.stage_counter := io.stage_counter
    
    bf0.io.a := io.in(0)
    bf0.io.b := io.in(1)
    bf0.io.wn := twid.io.wn(0)
    bf0.io.out_en := io.out_en
    io.out(0) := bf0.io.out0
    io.out(1) := bf0.io.out1
    
    
    bf1.io.a := io.in(2)
    bf1.io.b := io.in(3)
    bf1.io.wn := twid.io.wn(1)
    bf1.io.out_en := io.out_en
    io.out(2) := bf1.io.out0
    io.out(3) := bf1.io.out1
    
    bf2.io.a := io.in(4)
    bf2.io.b := io.in(5)
    bf2.io.wn := twid.io.wn(2)
    bf2.io.out_en := io.out_en
    io.out(4) := bf2.io.out0
    io.out(5) := bf2.io.out1
    
    bf3.io.a := io.in(6)
    bf3.io.b := io.in(7)
    bf3.io.wn := twid.io.wn(3)
    bf3.io.out_en := io.out_en
    io.out(6) := bf3.io.out0
    io.out(7) := bf3.io.out1
    
    
}


class Twiddle extends Module {
  val io = IO(new Bundle {
    val wn = Output(Vec(4, SInt(32.W)))
    val stage_counter = Input(UInt(2.W))
  })
  
  io.wn(0) := 0.S
  io.wn(1) := 0.S
  io.wn(2) := 0.S
  io.wn(3) := 0.S
  
  //temp values, will be hardcoded with proper twiddle factors one DP works
  val wn0 = 1.S;
  val wn1 = 2.S;
  val wn2 = 3.S;
  val wn3 = 4.S;
  
  switch(io.stage_counter){
    is(0.U){
      io.wn(0) := 1.S
      io.wn(1) := 1.S
      io.wn(2) := 1.S
      io.wn(3) := 1.S
    }
    is(1.U){
      io.wn(0) := wn0
      io.wn(1) := wn2
      io.wn(2) := wn0
      io.wn(3) := wn2
    }
    is(2.U){
      io.wn(0) := wn0
      io.wn(1) := wn1
      io.wn(2) := wn2
      io.wn(3) := wn3
    }
  }
}


/*object BF_block extends App{
  println("Generating BF_blocks hardware...")
  (new chisel3.stage.ChiselStage).emitVerilog(new BF_block(), Array("--target-dir", "generated"))
}*/
