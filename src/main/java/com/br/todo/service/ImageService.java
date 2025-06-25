package com.br.todo.service;

import com.br.todo.dto.image.ImageDTO;
import com.br.todo.entity.Image;
import com.br.todo.entity.Task;
import com.br.todo.entity.User;
import com.br.todo.exception.RegraDeNegocioException;
import com.br.todo.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final TaskService taskService;
    private final UserService userService;
    private final ModelMapper mapper;

    private static final List<String> TIPOS_PERMITIDOS = List.of("image/jpeg", "image/png");
    private static final long TAMANHO_MAXIMO = 2 * 1024 * 1024; // 2MB

    public ImageDTO UploadImageToTask(Integer idTask, MultipartFile image) throws IOException {
        Task task = taskService.getTask(idTask);

        validateImage(image);

        Image novaImagem = new Image();
        novaImagem.setDescricao(image.getOriginalFilename());
        novaImagem.setConteudoImagem(image.getBytes());
        novaImagem.setTarefa(task);

        Image imagemCriada = imageRepository.save(novaImagem);
        return mapper.map(imagemCriada, ImageDTO.class);
    }

    public void removeImageFromTask(Integer idImagem, String authHeader) {
        Image image = getImage(idImagem);
        User userSolicitante = userService.getLoggedUser(authHeader);

        if(!userSolicitante.getIdUsuario().equals(image.getTarefa().getUsuario().getIdUsuario())) {
            throw new RegraDeNegocioException("Você não tem permissão para excluir esta imagem.");
        }

        imageRepository.delete(image);
    }

    public Image getImage(Integer idImagem) {
        return imageRepository.findById(idImagem)
                .orElseThrow(() -> new RegraDeNegocioException("Nenhuma imagem com id " + idImagem + " encontrada."));
    }

    private void validateImage(MultipartFile image) {
        if (image.isEmpty()) {
            throw new IllegalArgumentException("Imagem vazia.");
        }

        if(!TIPOS_PERMITIDOS.contains(image.getContentType())) {
            throw new IllegalArgumentException("Tipo de imagem inválido. Use JPG ou PNG.");
        }

        if (image.getSize() > TAMANHO_MAXIMO) {
            throw new IllegalArgumentException("Imagem muito grande. Máximo permitido é 2MB.");
        }
    }
}
