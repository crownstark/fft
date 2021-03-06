package fft

import chisel3._
import chisel3.util._
import chisel3.iotesters.PeekPokeTester

class mainRegFileTester(dut: mainRegFile) extends PeekPokeTester(dut){
  poke(dut.io.load_en, 1.U)
  for (i <- 0 to 7){
    poke(dut.io.from_ROM(i), i.U)
    poke(dut.io.from_BF(i), (i+10).U)
  }
  poke(dut.io.demux_sel, 1.U)
  step(1)
  println("SEL = 1: FROM BUTTERFLY")
  for (i <- 0 to 7){
     
     println("to_evenOdd: " + peek(dut.io.to_evenOdd(i)).toString)
     step(1)
  }
  poke(dut.io.demux_sel, 0.U)
  step(1)
  println("SEL = 0: FROM ROM")
  for (i <- 0 to 7){
     println("to_evenOdd: " + peek(dut.io.to_evenOdd(i)).toString)
     step(1)
  }
}

object mainRegFileTester extends App {
  println("Testing mainRegFile...")
  iotesters.Driver.execute(Array[String](), () => new mainRegFile()){
    c => new mainRegFileTester(c)
  }
}

