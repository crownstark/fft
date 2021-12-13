package fft

import chisel3._
import chisel3.util._
import chisel3.experimental._

class mainRegFile extends Module{
  val io = IO(new Bundle {
      val demux_sel = Input(UInt(1.W)) // if 0 take from the ROM, if 1 take from the Butterfly Output
      val load_en = Input(UInt(1.W))
      val from_ROM = Input(Vec(8, SInt(32.W)))
      val from_BF = Input(Vec(8, SInt(32.W)))
      val to_evenOdd = Output(Vec(8, (SInt(32.W))))
  })
  

  for (i <- 0 until 8){
    io.to_evenOdd(i) := 0.S
  }
  
  when(io.load_en.asBool){
    when(io.demux_sel.asBool){
      for(i <- 0 until 8){
        io.to_evenOdd(i) := io.from_BF(i)
      }
    }
    .elsewhen(~(io.demux_sel.asBool)){
      for(i <- 0 until 8){
        io.to_evenOdd(i) := io.from_ROM(i)
      }
    }
  }
    
}
