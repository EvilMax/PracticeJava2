package org.practice.scala.domain;

import java.util.List;

public record Event(List<Address> recipients, Payload payload) {}
