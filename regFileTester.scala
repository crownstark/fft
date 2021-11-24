package fft

import chisel3._
import chisel3.util._
import chisel3.iotesters.PeekPokeTester

class regFileTester(dut: regFile4x32) extends PeekPokeTester(dut) {
/*	poke(dut.io.wraddr, 0.U)
	poke(dut.io.rdaddr0, 0.U)
	poke(dut.io.rdaddr1, 0.U)
	poke(dut.io.in, 0.U)
	poke(dut.io.out_en, 0.U)
	poke(dut.io.wr_en, 0.U)*/
	println("Init out0: " + peek(dut.io.out0).toString)
	println("Init out1: " + peek(dut.io.out1).toString)
	//step(1)
	for (i <- 0 to 7){
		poke(dut.io.wr_en, 1.U)
		poke(dut.io.in, i.U)
                poke(dut.io.wraddr, (i>>1))
                println("Iteration " + i.toString + ", wraddr = " + (i>>1).toString + ", in = " + i.toString)
		step(1)
	}
	for (j <- 0 to 3){
		poke(dut.io.out_en, 1.U)
		poke(dut.io.rdaddr0, j.U)
		poke(dut.io.rdaddr1, j.U)
		println("rdaddr = " + j.toString + " --- out0 = " + peek(dut.io.out0).toString)
		println("rdaddr = " + j.toString + " --- out1 = " +  peek(dut.io.out1).toString)
	}

}

object regFileTester extends App {
	println("Testing regFile")
	iotesters.Driver.execute(Array[String](), () => new regFile4x32()) {
		c => new regFileTester(c)
	}
}
