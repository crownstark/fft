package fft

import chisel3._
import chisel3.util._
import chisel3.experimental._
import chisel3.iotesters.PeekPokeTester

class twiddleRegTester(dut: twiddleRegFile) extends PeekPokeTester(dut){
  poke(dut.io.out_en, 1.U)
  for (i <- 0 to 7){
      poke(dut.io.rdaddr, i.U)
      println("Addr = " + i.toString + " --- out = " + peek(dut.io.out).toString)
  }
}

object twiddleRegTester extends App{
  println("Testing twiddleReg")
  iotesters.Driver.execute(Array[String](), () => new twiddleRegFile()){
    c => new twiddleRegTester(c)
  }
}
