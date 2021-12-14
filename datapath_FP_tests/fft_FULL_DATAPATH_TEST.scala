package fft

import chisel3._
import chisel3.util._
import chisel3.experimental._
import chisel3.iotesters.PeekPokeTester

class fft_FULL_DATAPATH_TEST(dut: fft_FULL_DATAPATH) extends PeekPokeTester(dut){
  
  
  for (i <- 0 until 8){
   pokeFixedPoint(dut.io.from_ROM(i), 0.0+i)
  }
  poke(dut.io.main_sel, 0.U)
  for (i <- 0 until 3){
    poke(dut.io.stage_counter, i.U) 
    poke(dut.io.main_loaden, 1.U)
    step(1)
    // should have data loaded from ROM to main now
    //poke(dut.io.main_loaden, 0.U) // main load enable low
    poke(dut.io.main_outen, 1.U) // dump main reg onto lines to even/odd
    step(1)
    poke(dut.io.even_wren, 1.U)
    poke(dut.io.odd_wren, 1.U)
    step(1)
    poke(dut.io.even_outen, 1.U) // dumps from e/o regfiles
    poke(dut.io.odd_outen, 1.U)
    poke(dut.io.main_sel, 1.U) // from Butterfly to main reg
    step(1)
    poke(dut.io.bf_outen, 1.U) // dumps from bf
    step(1)
    println("STAGE " + i.toString + ":")
    for (j <- 0 until 8){
      println("out(" + j.toString + "): " + peekFixedPoint(dut.io.out(j)).toString)
    }
  }
}

object fft_FULL_DATAPATH_TEST extends App{
  println("Testing DATAPATH (pls work)...")
  iotesters.Driver.execute(Array[String](), () => new fft_FULL_DATAPATH()){
    c => new fft_FULL_DATAPATH_TEST(c)
  }
}
