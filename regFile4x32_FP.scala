package fft

import chisel3._
import chisel3.util._
import chisel3.experimental._

/* 32'b 4 register register file -- to be converted to fixed point eventually -- hopefully
  2 outputs controlled by which stage its in and wheter or not the incrementer is about to roll over (cnt = 3'b111)
  write enable is controlled by the LSB of the incrementer (cnt[0])  from the EO module
*/

// TODO : DUPLICATE EVERYTHING TO SUPPORT 32.W 16.BP FIXED POINT
// TODO : DON'T FORGET TO INCLUDE chisel3.experimental._ or whatever
// TODO : Going to have to have 4 outputs, 4 inputs etc

/*class Complex extends Bundle {
  val re = FixedPoint(32.W, 16.BP)
  val im = FixedPoint(32.W, 16.BP)
}

class regFile4x32 extends Module {
  val io = IO(new Bundle {
    val wraddr = Input(UInt(2.W))
    val rdaddr0 = Input(UInt(2.W))
    val rdaddr1 = Input(UInt(2.W))
    val in = IO(Input(new Complex))
    val out_en = Input(UInt(1.W))
    val wr_en = Input(UInt(1.W))
    val out0 = IO(Output(new Complex))
    val out1 = IO(Output(new Complex))
  })
  io.out0.re := (0.U).asFixedPoint(16.BP)
  io.out0.im := (0.U).asFixedPoint(16.BP)
  io.out1.re := (0.U).asFixedPoint(16.BP)
  io.out1.im := (0.U).asFixedPoint(16.BP)
  val reg0 = RegInit(new Complex)
  val reg1 = RegInit(new Complex)
  val reg2 = RegInit(new Complex)
  val reg3 = RegInit(new Complex)
  //val reg2 = RegInit(0.U(32.W))
  //val reg3 = RegInit(0.U(32.W))
  when(io.wr_en.asBool){
    switch(io.wraddr){
       is(0.U) {reg0 := io.in
       	}
       is(1.U) {reg1 := io.in
       	}
       is(2.U) {reg2 := io.in
       	}
       is(3.U) {reg3 := io.in
       	}
    }
  }
  when(io.out_en.asBool){
    switch(io.rdaddr0){
        is(0.U) {io.out0 := reg0}
        is(1.U) {io.out0 := reg1}
        is(2.U) {io.out0 := reg2}
        is(3.U) {io.out0 := reg3}
    }
    switch(io.rdaddr1){
        is(0.U) {io.out1 := reg0}
        is(1.U) {io.out1 := reg1}
        is(2.U) {io.out1 := reg2}
        is(3.U) {io.out1 := reg3}
    }
  }
}*/

//object regFileMain extends App {
//  println("Generating the incrementer hardware")
//  (new chisel3.stage.ChiselStage).emitVerilog(new regFile4x32(), Array("--target-dir", "generated"))

//}
