package fft

import chisel3._
import chisel3.util._

/* 32'b 4 register register file -- to be converted to fixed point eventually -- hopefully
  2 outputs controlled by which stage its in and wheter or not the incrementer is about to roll over (cnt = 3'b111)
  write enable is controlled by the LSB of the incrementer (cnt[0])  from the EO module
*/

class regFile4x32 extends Module {
  val io = IO(new Bundle {
    val wraddr = Input(UInt(2.W))
    val rdaddr0 = Input(UInt(2.W))
    val rdaddr1 = Input(UInt(2.W))
    val in = Input(UInt(32.W))
    val out_en = Input(UInt(1.W))
    val wr_en = Input(UInt(1.W))
    val out0 = Output(UInt(32.W))
    val out1 = Output(UInt(32.W))
  })
  io.out0 := 0.U
  io.out1 := 0.U
  val reg0 = RegInit(0.U(32.W))
  val reg1 = RegInit(0.U(32.W))
  val reg2 = RegInit(0.U(32.W))
  val reg3 = RegInit(0.U(32.W))
  when(io.wr_en.asBool){
    switch(io.wraddr){
       is(0.U) {reg0 := io.in}
       is(1.U) {reg1 := io.in}
       is(2.U) {reg2 := io.in}
       is(3.U) {reg3 := io.in}
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
}


//object regFileMain extends App {
//  println("Generating the incrementer hardware")
//  (new chisel3.stage.ChiselStage).emitVerilog(new regFile4x32(), Array("--target-dir", "generated"))

//}
