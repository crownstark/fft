package fft

import chisel3._
import chisel3.util._

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
  })
  
  io.out_even := Vec(4, 0.U(32.W))
  io.out_odd := Vec(4, 0.U(32.W))
  
  val eRF = Module(new regFile4x32())
  val oRF = Module(new regFile4x32())
  
  eRF.io.out_en := io.rdy_forBF
  oRF.io.out_en := io.rdy_forBF
  
  eRF.io.wraddr := io.wr_addr
  oRF.io.wraddr := io.wr_addr
  
  eRF.io.in := io.in
  oRF.io.in := io.in
  
  eRF.io.wr_en := io.even_wren
  oRF.io.wr_en := io.odd_wren
  
  io.out_even(0) := eRF.io.out0
  io.out_even(1) := eRF.io.out1
  io.out_even(2) := eRF.io.out2
  io.out_even(3) := eRF.io.out3

  io.out_odd(0) := oRF.io.out0
  io.out_odd(1) := oRF.io.out1
  io.out_odd(2) := oRF.io.out2
  io.out_odd(3) := oRF.io.out3

  when(io.rdy_forBF.asBool){
    switch(io.stage_count){
      is(0.U){ //stage 0
        eRF.io.rdaddr0 := 0.U(2.W) // a for 1st even BF
        eRF.io.rdaddr1 := 2.U(2.W) // b for 1st even BF
        eRF.io.rdaddr2 := 1.U(2.W) // a for 2nd even BF
        eRF.io.rdaddr3 := 3.U(2.W) // b for 2nd even BF
      
        oRF.io.rdaddr0 := 0.U(2.W) // a for 1st odd BF
        oRF.io.rdaddr1 := 2.U(2.W) // b for 1st odd BF
        oRF.io.rdaddr2 := 1.U(2.W) // a for 2nd odd BF
        oRF.io.rdaddr3 := 3.U(2.W) // b for 2nd odd BF
      }
      is(1.U){ //stage 1
        eRF.io.rdaddr0 := 0.U(2.W) // a for 1st even BF
        eRF.io.rdaddr1 := 1.U(2.W) // b for 1st even BF
        eRF.io.rdaddr2 := 2.U(2.W) // a for 2nd even BF
        eRF.io.rdaddr3 := 3.U(2.W) // b for 2nd even BF
      
        oRF.io.rdaddr0 := 0.U(2.W) // a for 1st odd BF
        oRF.io.rdaddr1 := 1.U(2.W) // b for 1st odd BF
        oRF.io.rdaddr2 := 2.U(2.W) // a for 2nd odd BF
        oRF.io.rdaddr3 := 3.U(2.W) // b for 2nd odd BF
      }
      is(2.U){ //stage 0
        eRF.io.rdaddr0 := 0.U(2.W) // a for 1st even BF
        eRF.io.rdaddr1 := 2.U(2.W) // b for 1st even BF
        eRF.io.rdaddr2 := 1.U(2.W) // a for 2nd even BF
        eRF.io.rdaddr3 := 3.U(2.W) // b for 2nd even BF
      
        oRF.io.rdaddr0 := 0.U(2.W) // a for 1st odd BF
        oRF.io.rdaddr1 := 2.U(2.W) // b for 1st odd BF
        oRF.io.rdaddr2 := 1.U(2.W) // a for 2nd odd BF
        oRF.io.rdaddr3 := 3.U(2.W) // b for 2nd odd BF
      }
    }
  }
}
*/
  
