package controller;

import model.builder.ReceitaBuilder;
import model.dao.DAOFactory;
import model.dao.interfaces.ReceitaDao;
import model.entities.Ingrediente;
import model.entities.Receita;
import view.ReceitaView;
import java.util.ArrayList;

public class ReceitaController {

    private ReceitaView receitaView;

    public ReceitaController(ReceitaView receitaView) {
        this.receitaView = receitaView;
        initButtonListeners();
    }

    private void initButtonListeners() {

        receitaView.addCancelarButtonActionListener(e -> receitaView.dispose());

        receitaView.addAdicionarButtonActionListener(e -> {

            String titulo = receitaView.getTxtTitulo();
            String descricao = receitaView.getTxtDescricao();
            ArrayList<Ingrediente> ingredientes = new ArrayList<>();
            String modoPreparo = "";
            double tempoPreparo = 0;
            String emailAutor = "";
            ReceitaBuilder receitaBuilder = new ReceitaBuilder(titulo)
                    .descricao(descricao)
                    .ingredientes(ingredientes)
                    .instrucoes(modoPreparo)
                    .tempoPreparo(tempoPreparo)
                    .emailAutor(emailAutor);

            Receita receita = new Receita(receitaBuilder);
            ReceitaDao receitaDao = DAOFactory.createReceitaDao();
            receitaDao.create(receita);
            receitaView.dispose();
        });

    }

}
