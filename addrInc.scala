package fft

import chisel3._
import chisel3.util._


class addrInc extends Module {
  val io = IO(new Bundle {
    val rdaddr = Output(UInt(3.W))
    val rd_shift = Output(UInt(2.W))
    val rd_from = Output(UInt(1.W))
    val rdy = Output(UInt(1.W))
  })

  io.rd_shift := 0.U
  io.rd_from := 0.U
  io.rdaddr := 0.U
  io.rdy := 0.U  
  val reggie = RegInit(0.U(3.W))
  reggie := reggie + 1.U
  io.rd_shift := reggie >> 1
  io.rd_from := reggie(0)
  io.rdy := reggie(2)&reggie(1)&reggie(0)
}

object incMain extends App {
  println("Generating the incrementer hardware")
  (new chisel3.stage.ChiselStage).emitVerilog(new addrInc(), Array("--target-dir", "generated"))

}
