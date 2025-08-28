package com.senai.oficina_teste_swagger.interface_ui.controle;

import com.senai.oficina_teste_swagger.application.dto.ServicoDTO;
import com.senai.oficina_teste_swagger.application.service.ServicoAppService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;


import java.util.List;

@Tag(name = "Serviços", description = "Gerenciamento de serviços da oficina mecânica")
@RestController
@RequestMapping("/servicos")
public class ServicoController {
    private final ServicoAppService service;

    public ServicoController(ServicoAppService service) {
        this.service = service;
    }

    @Operation(
            summary = "Cadastrar um novo serviço",
            description = "Adiciona um novo serviço à base de dados após validações de preço e duração",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ServicoDTO.class),
                            examples = @ExampleObject(value = """
                                        {
                                          "descricao": "Troca de óleo",
                                          "preco": 120.00,
                                          "dataInicio": "2025-08-05",
                                          "dataFim": "2025-08-05"
                                        }
                                    """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Serviço cadastrado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Violação de regras de negócio")
            }
    )
    @PostMapping
    public ResponseEntity<ServicoDTO> criar(@Valid @org.springframework.web.bind.annotation.RequestBody ServicoDTO dto) {

        return ResponseEntity
                .status(201)
                .body(service.salvar(dto));
    }

    @Operation(
            summary = "Listar todos os serviços",
            description = "Retorna todos os serviços cadastrados"
    )
    @GetMapping
    public List<ServicoDTO> listar() {
        return service.listar();
    }

    @Operation(
            summary = "Buscar serviço por ID",
            description = "Retorna um serviço existente a partir do seu ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Serviço encontrado"),
                    @ApiResponse(responseCode = "404", description = "Serviço não encontrado")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ServicoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(
            summary = "Atualizar um serviço",
            description = "Atualiza os dados de um serviço existente com novas informações",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = ServicoDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Serviço atualizado"),
                    @ApiResponse(responseCode = "400", description = "Violação de regras de negócio")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ServicoDTO> atualizar(@PathVariable Long id, @org.springframework.web.bind.annotation.RequestBody ServicoDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @Operation(
            summary = "Deletar um serviço",
            description = "Remove um serviço da base de dados a partir do seu ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Serviço removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Serviço não encontrado")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
