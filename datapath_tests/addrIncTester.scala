package fft

import chisel3._
import chisel3.util._
import chisel3.iotesters.PeekPokeTester

class addrIncTester(dut: addrInc) extends PeekPokeTester(dut) {
	for (i <- 0 to 15){
		println("it: " + i.toString)
		println("rdaddr: " + peek(dut.io.rdaddr).toString)
		println("rd_shift: " + peek(dut.io.rd_shift).toString)
		println("rd_from: " + peek(dut.io.rd_from).toString)
		println("rdy: " + peek(dut.io.rdy).toString)
		step(1)
	}
}

object addrIncTester extends App {
	println("Testing addrInc")
	iotesters.Driver.execute(Array[String](), () => new addrInc()) {
		c => new addrIncTester(c)
	}
}
