package org.example.infrastructure.test.classes;

import org.example.infrastructure.TypeSpecificService;
import org.springframework.stereotype.Service;

@Service
public class BaseService<T extends AbstractClass> implements TypeSpecificService<T> {
}
