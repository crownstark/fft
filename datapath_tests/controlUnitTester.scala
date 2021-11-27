package fft

import chisel3._
import chisel3.util._
import chisel3.experimental._
import chisel3.iotesters.PeekPokeTester

class controlUnitTester(dut: controlUnit) extends PeekPokeTester(dut){
  println("START_FFT = 0, STAGE_COMPLETE = 0")
  poke(dut.io.START_FFT, 0.U)
  println("STAGE_COUNT = " + peek(dut.io.STAGE_COUNT).toString)
  println("DUMP_OUTPUT = " + peek(dut.io.DUMP_OUTPUT).toString)
  println("COUNT_EN = " + peek(dut.io.COUNT_EN).toString)
  println("START_FFT = 1")
  for (i <- 0 to 3){
    println("STAGE_COUNT = " + peek(dut.io.STAGE_COUNT).toString)
    println("DUMP_OUTPUT = " + peek(dut.io.DUMP_OUTPUT).toString)
    println("COUNT_EN = " + peek(dut.io.COUNT_EN).toString)
  }

}

object controlUnitTester extends App {
  println("Testing Control Unit")
  iotesters.Driver.execute(Array[String](), () => new controlUnit()){
    c => new controlUnitTester(c)
  }
}

