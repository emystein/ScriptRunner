SELECT post.title, author.name as author FROM post, author WHERE post.author_id = author.id ORDER BY post.title;
