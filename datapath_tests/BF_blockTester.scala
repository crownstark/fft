package fft

import chisel3._
import chisel3.util._
import chisel3.iotesters.PeekPokeTester

class BF_blockTester(dut: BF_block) extends PeekPokeTester(dut){

  for (i <- 0 until 8){
    poke(dut.io.in(i), i.S)
  }

  for (i <- 0 until 3){
    poke(dut.io.stage_counter, i.U)
    println("STAGE : " + i.toString)
    step(1)
    poke(dut.io.out_en, 1.U)
    for (j <- 0 until 8){
      println("out(" + j.toString + "): " + peek(dut.io.out(j)).toString)
    }
  }
  
}

object BF_blockTester extends App {
  println("Testing BF_block...")
  iotesters.Driver.execute(Array[String](), () => new BF_block()) {
    c => new BF_blockTester(c)
  }
}


