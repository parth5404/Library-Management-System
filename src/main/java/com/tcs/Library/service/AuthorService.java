package com.tcs.Library.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tcs.Library.entity.Author;
import com.tcs.Library.repository.AuthorRepo;
import com.tcs.Library.dto.AuthorSignUp;
import com.tcs.Library.dto.wrapper.AuthorMapper;
import com.tcs.Library.entity.*;

@Service
public class AuthorService {
    @Autowired
    private AuthorRepo authorDS;

    public List<Book> getByAuthorId(Long id) {
        Author author = authorDS.getById(id);
        if (author == null) {
            throw new Error("No Author for this ID");
        }
        return author.getBook();
    }
    public Author registerAuthor(AuthorSignUp dto){
        Author author= AuthorMapper.toEntity(dto);
        authorDS.save(author);
        return author;
    }
}
