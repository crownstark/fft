package fft

import chisel3._
import chisel3.util._
import chisel3.iotesters.PeekPokeTester

class twiddleTester(dut: Twiddle) extends PeekPokeTester(dut){
  for(i <- 0 until 3){
    poke(dut.io.stage_counter, i.U)
    println("STAGE: " + i.toString)
    for(i <- 0 until 4){
      println("wn"+ i.toString + ": " + peek(dut.io.wn(i)).toString)
    }
  }
}

object twiddleTester extends App{
  println("Testing Twiddle Block...")
  iotesters.Driver.execute(Array[String](), () => new Twiddle()){
    c => new twiddleTester(c)
  }
}
