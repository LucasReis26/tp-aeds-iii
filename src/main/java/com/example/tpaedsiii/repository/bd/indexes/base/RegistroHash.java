package com.example.tpaedsiii.repository.bd.indexes.base;
import java.io.IOException;

public interface RegistroHash<T> {

  public int hashCode(); 

  public short size(); 

  public byte[] toByteArray() throws IOException; 

  public void fromByteArray(byte[] ba) throws IOException; 

  void setId(int id);
}