package com.lucasangelo.todosimple.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lucasangelo.todosimple.models.User;
import com.lucasangelo.todosimple.repositories.UserRepository;
import com.lucasangelo.todosimple.services.exceptions.DataBindingViolationException;
import com.lucasangelo.todosimple.services.exceptions.ObjectNotFoundException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findById(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        return user.orElseThrow(() -> new ObjectNotFoundException(
                "Usuario não encontrado! Id: " + id + ", Tipo: " + User.class.getName()));
    }

    @Transactional
    // Sempre usar quando for salvar algo no banco:
    // Create, update
    public User create(User obj) {
        obj.setId(null); // ! Importante para casos onde nao tem verificaçao e a propria task tem um id.
                         // ! Sem isso, pode acabar dando erro ao tentar adicionar o objeto e ele ja ter
                         // ! um id feito pelo usuario
        obj = this.userRepository.save(obj);
        return obj;
    }

    @Transactional
    public User update(User obj) {
        User newObj = findById(obj.getId());
        newObj.setPassword(obj.getPassword());
        return this.userRepository.save(newObj);
    }

    public void delete(Long id) {
        findById(id);
        try {
            this.userRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas!");
        }
    }
}
