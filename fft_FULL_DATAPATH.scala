package fft 

import chisel3._
import chisel3.util._
import chisel3.experimental._

// main regfile -- now sold with floating point!
class mainRF_FP extends Module {
  val io = IO(new Bundle {
    val sel = Input(UInt(1.W))
    val load_en = Input(UInt(1.W))
    val out_en = Input(UInt(1.W))
    val rdaddr = Input(UInt(3.W))
    val from_ROM = Input(Vec(8, FixedPoint(32.W, 16.BP)))
    val from_BF = Input(Vec(8, FixedPoint(32.W, 16.BP)))
    val to_evenOdd = Output(FixedPoint(32.W, 16.BP))
  })
  
  /*for (i <- 0 until 8){
    io.to_evenOdd(i) := 0.F(32.W, 16.BP) // this is how to initialize FP values
  }*/
  io.to_evenOdd := 0.F(32.W, 16.BP)
  
  // initialized register file of FP numbers
  val regx = RegInit(VecInit(Seq.fill(8)(0.F(32.W, 16.BP))))
  
  // from BF when sel high
  // from ROM when sel low
  when(io.load_en.asBool){
    when(io.sel.asBool){
      for (i <- 0 until 8){
        regx(i) := io.from_BF(i)
      }
    }
    .elsewhen(~(io.sel.asBool)){
      for (i <- 0 until 8){
        regx(i) := io.from_ROM(i)
      }
    }
  }
  
  when(io.out_en.asBool){
    switch(io.rdaddr){
      is(0.U) {io.to_evenOdd := regx(0)}
      is(1.U) {io.to_evenOdd := regx(1)}
      is(2.U) {io.to_evenOdd := regx(2)}
      is(3.U) {io.to_evenOdd := regx(3)}
      is(4.U) {io.to_evenOdd := regx(4)}
      is(5.U) {io.to_evenOdd := regx(5)}
      is(6.U) {io.to_evenOdd := regx(6)}
      is(7.U) {io.to_evenOdd := regx(7)}
    }
  }
}

// address incrementer 
class addrInc_FP extends Module {
  val io = IO(new Bundle {
    val mainReg_rdaddr = Output(UInt(3.W))
    val evenOdd_wraddr = Output(UInt(2.W))
    val evenOdd_wren = Output(UInt(1.W)) // contols even/odd write enable tied to LSb of counter
    val count_done = Output(UInt(1.W))
    val count_start = Input(UInt(1.W))
  })
  
  io.mainReg_rdaddr := 0.U
  io.evenOdd_wraddr := 0.U
  io.evenOdd_wren := 0.U
  io.count_done := 0.U
  
 
  val count_en = RegInit(0.U(1.W))
  val counter = RegInit(0.U(4.W))
  val done = counter(3)
  io.count_done := done
  
  when(io.count_start.asBool){
    count_en := 1.U
  }
  when(done.asBool){
    count_en := 0.U
    counter := 0.U
  }
  
  when(count_en.asBool){
    when(~(done.asBool)){
      io.mainReg_rdaddr := counter
      io.evenOdd_wraddr := counter >> 1
      io.evenOdd_wren := counter(0)
      counter := counter + 1.U
    }
    .elsewhen(done.asBool){
      io.count_done := 1.U
    }
  }
}

// one input, 4 output 4x32 Reg File -- serve as even/odd reg files
class regFile4x32_FP extends Module {
  val io = IO(new Bundle {
    val wraddr = Input(UInt(2.W))
    val rdaddr = Input(Vec(4, UInt(2.W)))
    val in = Input(FixedPoint(32.W, 16.BP))
    val out_en = Input(UInt(1.W))
    val wren = Input(UInt(1.W))
    val out = Output(Vec(4, FixedPoint(32.W, 16.BP)))
  })
  
  for (i <- 0 until 4){
    io.out(i) := 0.F(32.W, 16.BP)
  }
  
  val reg = RegInit(VecInit(Seq.fill(4)(0.F(32.W, 16.BP))))
  
  when(io.wren.asBool){
    switch(io.wraddr){
      is(0.U){reg(0) := io.in}
      is(1.U){reg(1) := io.in}
      is(2.U){reg(2) := io.in}
      is(3.U){reg(3) := io.in}
    }
  }
  
  when(io.out_en.asBool){
    for (i <- 0 until 4){
      switch(io.rdaddr(i)){
        is(i.U){io.out(i) := reg(i)}
      }
    }
  }
}

// 12/13/2021 2:49 AM
// TODO: EVEN/ODD SPLIT FUNCTIONALITY AND TEST
// TODO: BUTTERFLY BLOCKS WITH FP AND TEST
// TODO: CONNECT ALL AND TEST
    
    
