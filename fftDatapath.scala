package fft

import chisel3._
import chisel3.util._

class RF_4x32 extends Module {
  val io = IO(new Bundle {
    val wr_addr = Input(UInt(2.W))
    val rdaddr = Input(Vec(4, UInt(2.W)))
    val in = Input(UInt(32.W))
    val out_en = Input(UInt(1.W))
    val wr_en = Input(UInt(1.W))
    val out = Output(Vec(4, (UInt(32.W))))
  })
  
  for (i <- 0 until 4){io.out(i) := 0.U}
    
  val regx = RegInit(VecInit(Seq.fill(4)(0.U(32.W))))
  
  when(io.wr_en.asBool){
    switch(io.wr_addr){
      is(0.U) {regx(0) := io.in}
      is(1.U) {regx(1) := io.in}
      is(2.U) {regx(2) := io.in}
      is(3.U) {regx(3) := io.in}
    }
  }

  when(io.out_en.asBool){
    switch(io.rdaddr(0)){
      is(0.U){io.out(0) := regx(0)}
      is(1.U){io.out(0) := regx(1)}
      is(2.U){io.out(0) := regx(2)}
      is(3.U){io.out(0) := regx(3)}
    }
    switch(io.rdaddr(1)){
      is(0.U){io.out(1) := regx(0)}
      is(1.U){io.out(1) := regx(1)}
      is(2.U){io.out(1) := regx(2)}
      is(3.U){io.out(1) := regx(3)}
    }
    switch(io.rdaddr(2)){
      is(0.U){io.out(2) := regx(0)}
      is(1.U){io.out(2) := regx(1)}
      is(2.U){io.out(2) := regx(2)}
      is(3.U){io.out(2) := regx(3)}
    }
    switch(io.rdaddr(3)){
      is(0.U){io.out(3) := regx(0)}
      is(1.U){io.out(3) := regx(1)}
      is(2.U){io.out(3) := regx(2)}
      is(3.U){io.out(3) := regx(3)}
    }
  }
      
   
}

class incRedo extends Module {
  val io = IO(new Bundle {
    val count_en = Input(UInt(1.W))
    val eo_wren = Output(UInt(1.W)) // write enable to even-odd reg
    val eo_wraddr = Output(UInt(2.W))
    val count_done = Output(UInt(1.W)) // sent to CU which flips count_en low, until stage increments
    val mainReg_rdaddr = Output(UInt(3.W))
  })
  
  io.eo_wren := 0.U
  io.eo_wraddr := 0.U
  io.mainReg_rdaddr := 0.U
  
  val counter = RegInit(0.U(3.W))
  when (io.count_en.asBool){
    io.mainReg_rdaddr := counter
    io.eo_wren := counter(0)
    io.eo_wraddr := counter >> 1
    io.count_done := counter(2)&counter(1)&counter(0)
    when(~io.count_done.asBool & io.count_en.asBool){
        counter := counter + 1.U
    }
  }
}

class EO extends Module {
  val io = IO(new Bundle {
    //val e_wren = Input(UInt(1.W))
    //val o_wren = Input(UInt(1.W))
    //val eo_wraddr = Input(UInt(2.W))
    val stage_counter = Input(UInt(2.W)) // control sig
    val to_evenOdd = Input(UInt(32.W))
    val out_toBF = Output(Vec(8, UInt(32.W))
    val outen_toBF = Input(UInt(1.W)) // control sig
    val mainReg_rdaddr = Output(UInt(3.W))
    val count_done = Output(UInt(1.W))
  })
  
  for(i <- 0 until 8){
    io.out_toBF(i) := 0.U
  }
  
  val eRF = Module(new RF_4x32())
  val oRF = Module(new RF_4x32())
  
  eRF.io.in := io.to_evenOdd
  oRF.io.in := io.to_evenOdd
  
  val inc = Module(new incRedo())
  
  io.mainReg_rdaddr := inc.io.mainReg_rdaddr
  io.count_done := inc.io.count_done
  
  eRF.io.wr_en := ~inc.io.wren
  oRF.io.wr_en := inc.io.wren
  
  eRF.io.wr_addr := inc.io.eo_wraddr
  oRF.io.wr_addr := inc.io.eo_wraddr
  
  when(io.outen_toBF.asBool){
    switch(io.stage_counter){
      is(0.U){
        eRF.io.rdaddr(0) := 0.U //a in 1st even BF
        eRF.io.rdaddr(1) := 2.U //b in 1st even BF
        eRF.io.rdaddr(2) := 1.U //a in 2nd even BF
        eRF.io.rdaddr(3) := 3.U //b in 2nd even BF
        
        oRF.io.rdaddr(0) := 0.U // " " odd " "
        oRF.io.rdaddr(1) := 2.U
        oRF.io.rdaddr(2) := 1.U
        oRF.io.rdaddr(3) := 3.U
      }
      is(1.U){
        eRF.io.rdaddr(0) := 0.U
        eRF.io.rdaddr(1) := 1.U
        eRF.io.rdaddr(2) := 2.U
        eRF.io.rdaddr(3) := 3.U
        
        oRF.io.rdaddr(0) := 0.U
        oRF.io.rdaddr(1) := 1.U
        oRF.io.rdaddr(2) := 2.U
        oRF.io.rdaddr(3) := 3.U
      }
      is(2.U){
        eRF.io.rdaddr(0) := 0.U
        eRF.io.rdaddr(1) := 2.U
        eRF.io.rdaddr(2) := 1.U
        eRF.io.rdaddr(3) := 3.U
        
        oRF.io.rdaddr(0) := 0.U
        oRF.io.rdaddr(1) := 2.U
        oRF.io.rdaddr(2) := 1.U
        oRF.io.rdaddr(3) := 3.U
      }
      
  }
}

class BF_blocks extends Module {
  val io = IO(new Bundle {
   
  })
  
  val WN = Module(new twiddleRegFile())
  
  val BF0 = Module(new Butterfly())
  val BF1 = Module(new Butterfly())
  val BF2 = Module(new Butterfly())
  val BF3 = Module(new Butterfly())
  
}



/*class fftDatapath extends Module {
  val io = IO(new Bundle {
    val BF_ROM_sel = Input(UInt(1.W))
    val ROM_out_en = Input (UInt(1.W))
    val RAM_load_en = Input(UInt(1.W))
    val stage_counter = Input(UInt(2.W))
    val count_done = Output(UInt(1.W))
    
  })
  
  val EO = Module(new EO())
  EO.io.stage_counter := io.stage_counter
  io.count_done := EO.io.count_done
  val ROM = Module(new SigGen())
  ROM.io.out_en := io.ROM_out_en
  val RAM = Module(new mainRegFile())
  RAM.io.demux_sel := io.BF_ROM_sel // control signal controlling whether MRF is loaded from BF or ROM
  RAM.io.load_en := io.RAM_load_en
  val BF = Module(new BF_Blocks())
  BF.io.stage_counter := io.stage_counter
  
  /*    val stage_counter = Input(UInt(2.W)) // control sig
    val to_evenOdd = Input(UInt(32.W))
    val out_toBF = Output(Vec(8, UInt(32.W))
    val outen_toBF = Input(UInt(1.W)) // control sig
    val mainReg_rdaddr = Output(UInt(3.W))*/
    
  
  
  RAM.io.mainReg_rdaddr := EO.io.mainReg_rdaddr
  RAM.io.from_ROM := ROM.io.sig_out
  RAM.io.from_BF := BF.io.out
    
  
  EO.io.to_evenOdd := RAM.io.to_evenOdd
  
  
  
  
    
}*/


/*class fftDatapath extends Module {
  val io = IO(new Bundle {
    val stage_count = Input(UInt(2.W))
    val out_even = Output(Vec(4, UInt(32.W)))
    val out_odd = Output(Vec(4, UInt(32.W)))
  })
  
  /*class SigGen extends Module {
  val io = IO(new Bundle {
    val out_en = Input(UInt(1.W))
    val sig_out = Output(Vec(8, UInt(32.W)))
  })*///CONNECTED
  
  val sig = Module(new SigGen())
  sig.io.out_en := 1.U
  
  val inc = Module(new addrInc())
  inc.io.count_en := 1.U
  
  /*class mainRegFile extends Module{
  val io = IO(new Bundle {
      val demux_sel = Input(UInt(1.W)) // if 0 take from the ROM, if 1 take from the Butterfly Output
      val load_en = Input(UInt(1.W))
      val from_ROM = Input(Vec(8, UInt(32.W)))
      val from_BF = Input(Vec(8, UInt(32.W)))
      val mainReg_rdaddr = Input(UInt(3.W)) // from incrementer
      val to_evenOdd = Output(UInt(32.W))
  })*/
  val mem = Module(new mainRegFile())
  //mem.io.from_BF := Vec(8, 0.U(32.W))
  for (i <- 0 until 8){
    mem.io.from_BF(i) := 0.U
    mem.io.from_ROM(i) := sig.io.sig_out(i)
  }
  mem.io.demux_sel := 0.U
  mem.io.mainReg_rdaddr := inc.io.mainReg_rdaddr
  mem.io.load_en := 1.U
  
  /*class addrInc extends Module {
  val io = IO(new Bundle {
    val mainReg_rdaddr = Output(UInt(3.W))
    val evenOdd_wraddr = Output(UInt(2.W))
    val evenOdd_wr_en = Output(UInt(1.W)) // controls whether EVEN or ODD gets written too -- tied to LSb of incrementer
    val rdy_forBF = Output(UInt(1.W))
    val count_en = Input(UInt(1.W))
  })*/
 
  
  val EO = Module(new EO_RegFile())
  EO.io.in := mem.io.to_evenOdd
  EO.io.even_wren := ~inc.io.evenOdd_wr_en
  EO.io.odd_wren := inc.io.evenOdd_wr_en
  EO.io.wr_addr := inc.io.evenOdd_wraddr
  EO.io.rdy_forBF := inc.io.rdy_forBF
  EO.io.stage_count := io.stage_count
  for (i <- 0 until 8){
  io.out_even(i) := EO.io.out_even(i)
  io.out_odd(i) := EO.io.out_odd(i)
  }
  
  
  /*class EO_RegFile extends Module {
  val io = IO(new Bundle {
    val stage_count = Input(UInt(2.W))
    val rdy_forBF = Input(UInt(1.W))
    val in = Input(UInt(32.W))
    val wr_addr = Input(UInt(2.W))
    val even_wren = Input(UInt(1.W))
    val odd_wren = Input(UInt(1.W))
    val out_even = Output(Vec(4, UInt(32.W)))
    val out_odd = Output(Vec(4, UInt(32.W)))
  })*/  
}*/
