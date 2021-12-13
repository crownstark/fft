package fft

import chisel3._
import chisel3.util._
import chisel3.experimental._

class SigGen extends Module {
  val io = IO(new Bundle {
    val out_en = Input(UInt(1.W))
    val sig_out = Output(Vec(8, UInt(32.W)))
  })
  
  for (i <- 0 to 7){
    io.sig_out(i) := i.U
  }
}
  
        
        

