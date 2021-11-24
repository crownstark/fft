package fft

import chisel3._
import chisel3.util._
class Mux4 extends Module {
     val io = IO(new Bundle {
         val in0 = Input(UInt(8.W))
         val in1 = Input(UInt(8.W))
	 val in2 = Input(UInt(8.W))
	 val in3 = Input(UInt(8.W))
	 val sel = Input(UInt(2.W))
	 val out = Output(UInt(8.W))
})

    val sel = io.sel
    val i0 = io.in0
    val i1 = io.in1
    val i2 = io.in2
    val i3 = io.in3
    val out = Wire(UInt(8.W))
    out := 0.U

    switch(sel) {
	 is(0.U) {out := i0}
	 is(1.U) {out := i1}
	 is(2.U) {out := i2}
	 is(3.U) {out := i3}
    }
    io.out := out
}

object Mux4Main extends App {
  println("Generating the Mux4?  hardware")
  (new chisel3.stage.ChiselStage).emitVerilog(new Mux4(), Array("--target-dir", "generated"))
}
