package fft

import chisel3._
import chisel3.util._
import chisel3.experimental._

class Butterfly extends Module {
  val io = IO(new Bundle {
    val a = Input(SInt(32.W))
    val b = Input(SInt(32.W))
    val wn = Input(SInt(32.W))
    val out0 = Output(SInt(32.W))
    val out1 = Output(SInt(32.W))
    val out_en = Input(UInt(1.W))
  })
  
  io.out0 := 0.S
  io.out1 := 0.S
  
  when(io.out_en.asBool){
    io.out0 := io.a + io.b*io.wn
    io.out1 := io.a - io.b*io.wn
  }
}
