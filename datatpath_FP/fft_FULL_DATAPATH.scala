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
// TODO: CONNECT ALL AND TEST


// single butterfly block with fixedpoint
class BF_FP extends Module {
  val io = IO(new Bundle {
    val a = Input(FixedPoint(32.W, 16.BP))
    val b = Input(FixedPoint(32.W, 16.BP))
    val wn = Input(FixedPoint(32.W, 16.BP))
    val out0 = Output(FixedPoint(32.W, 16.BP))
    val out1 = Output(FixedPoint(32.W, 16.BP))
    val out_en = Input(UInt(1.W))
  })
  
  io.out0 := 0.F(32.W, 16.BP)
  io.out1 := 0.F(32.W, 16.BP)
  
  when(io.out_en.asBool){
    io.out0 := io.a + io.b*io.wn
    io.out1 := io.a - io.b*io.wn
  }
}

class twiddleROM_FP extends Module {
  val io = IO(new Bundle {
    val wn = Output(Vec(4, FixedPoint(32.W, 16.BP)))
    val stage_counter = Input(UInt(2.W))
  }) // maybe out_en also??
  
  for (i <- 0 until 4){
    io.wn(i) := 0.F(32.W, 16.BP)
  }
  
  // random values, will be changed to be real values later on
  val wn0 = (1.1).F(32.W, 16.BP)
  val wn1 = (2.2).F(32.W, 16.BP)
  val wn2 = (3.3).F(32.W, 16.BP)
  val wn3 = (4.4).F(32.W, 16.BP)
  
  switch(io.stage_counter){
    is(0.U){
      for(i <- 0 until 4){
        io.wn(i) := 1.F(32.W, 16.BP)
      }
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
  
// TODO: VECTOR OF MODULES NOT WORKING
class BF_blocks_FP extends Module {
  val io = IO(new Bundle {
    val in = Input(Vec(8, FixedPoint(32.W, 16.BP)))
    val out = Output(Vec(8, FixedPoint(32.W, 16.BP)))
    val wn = Input(FixedPoint(32.W, 16.BP))
    val stage_counter = Input(UInt(2.W))
    val out_en = Input(UInt(1.W))
  })
  
  for (i <- 0 until 8){
    io.out(i) := 0.F(32.W, 16.BP)
  }

  
  val bf0 = Module(new BF_FP())
  val bf1 = Module(new BF_FP())
  val bf2 = Module(new BF_FP())
  val bf3 = Module(new BF_FP())
  
  val twid = Module(new twiddleROM_FP())
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

class mainRF_redesign_FP extends Module {
  val io = IO(new Bundle {
    val in = Input(Vec(8, FixedPoint(32.W, 16.BP)))
    val out = Output(Vec(8, FixedPoint(32.W, 16.BP)))
    val load_en = Input(UInt(1.W))
    val sel = Input(UInt(1.W))
    val out_en = Input(UInt(1.W))
    val from_ROM = Input(Vec(8, FixedPoint(32.W, 16.BP)))
    val from_BF = Input(Vec(8, FixedPoint(32.W, 16.BP)))
  })
  
  for (i <- 0 until 8){
    io.out(i) := 0.F(32.W, 16.BP)
  }
  
  val regx = RegInit(VecInit(Seq.fill(8)(0.F(32.W, 16.BP)))) //initialization of registers
  
  // inputs -- sel = 1 gets from BF
  // inputs -- sel = 0 gets from "ROM"
  when(io.load_en.asBool){
    when(io.sel.asBool){
      for(i <- 0 until 8){
        regx(i) := io.from_BF(i)
      }
    }
    .elsewhen(~(io.sel.asBool)){
      for(i <- 0 until 8){
        regx(i) := io.from_ROM(i)
      }
    }
  }
  
  // just straight up tied the even and odds
  // evens are 0-3 ties these straight to the 4 inputs of 4x32RF
  // odds  are 4-7
  when(io.out_en.asBool){
    io.out(0) := regx(0)
    io.out(1) := regx(2)
    io.out(2) := regx(4)
    io.out(3) := regx(6)
    io.out(4) := regx(1)
    io.out(5) := regx(3)
    io.out(6) := regx(5)
    io.out(7) := regx(7)
  }
}


    
  
    
    
