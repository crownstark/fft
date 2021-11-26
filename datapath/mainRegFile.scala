package fft

import chisel3._
import chisel3.util._
import chisel3.experimental._

class mainRegFile extends Module{
  val io = IO(new Bundle {
      val demux_sel = Input(UInt(1.W)) // if 0 take from the ROM, if 1 take from the Butterfly Output
      val load_en = Input(UInt(1.W))
      val from_ROM = Input(Vec(8, UInt(32.W)))
      val from_BF = Input(Vec(8, UInt(32.W)))
      val mainReg_rdaddr = Input(UInt(3.W)) // from incrementer
      val to_evenOdd = Output(UInt(32.W))
  })
  
  val reg0 = RegInit(0.U(32.W))
  val reg1 = RegInit(0.U(32.W))
  val reg2 = RegInit(0.U(32.W))
  val reg3 = RegInit(0.U(32.W))
  val reg4 = RegInit(0.U(32.W))
  val reg5 = RegInit(0.U(32.W))
  val reg6 = RegInit(0.U(32.W))
  val reg7 = RegInit(0.U(32.W))
  
  io.to_evenOdd := 0.U
  
  switch(io.mainReg_rdaddr){
    is(0.U){io.to_evenOdd := reg0}
    is(1.U){io.to_evenOdd := reg1}
    is(2.U){io.to_evenOdd := reg2}
    is(3.U){io.to_evenOdd := reg3}
    is(4.U){io.to_evenOdd := reg4}
    is(5.U){io.to_evenOdd := reg5}
    is(6.U){io.to_evenOdd := reg6}
    is(7.U){io.to_evenOdd := reg7}
  }
  
  when(io.load_en.asBool){
    when(io.demux_sel.asBool){
      reg0 := io.from_BF(0)
      reg1 := io.from_BF(1)
      reg2 := io.from_BF(2)
      reg3 := io.from_BF(3)
      reg4 := io.from_BF(4)
      reg5 := io.from_BF(5)
      reg6 := io.from_BF(6)
      reg7 := io.from_BF(7)
    }
    .elsewhen(~(io.demux_sel.asBool)){
      reg0 := io.from_ROM(0)
      reg1 := io.from_ROM(1)
      reg2 := io.from_ROM(2)
      reg3 := io.from_ROM(3)
      reg4 := io.from_ROM(4)
      reg5 := io.from_ROM(5)
      reg6 := io.from_ROM(6)
      reg7 := io.from_ROM(7)
    }
  }
    
}
