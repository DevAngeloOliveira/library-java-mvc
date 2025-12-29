// Configuracao da API
const API_URL = 'http://localhost:8080';

// Carregar dados ao iniciar
document.addEventListener('DOMContentLoaded', () => {
    carregarTodos();
    carregarDisponiveis();
    carregarEmprestados();
});

// Funcao para mostrar abas
function mostrarAba(aba) {
    // Remover active de todos
    document.querySelectorAll('.tab-button').forEach(btn => btn.classList.remove('active'));
    document.querySelectorAll('.tab-content').forEach(content => content.classList.remove('active'));
    
    // Ativar selecionado
    event.target.classList.add('active');
    document.getElementById(aba).classList.add('active');
}

// Carregar todos os itens
async function carregarTodos() {
    try {
        const response = await fetch(`${API_URL}/api/itens`);
        const apiResponse = await response.json();
        const itens = apiResponse.dados || [];
        renderizarItens(itens, 'lista-todos', true);
    } catch (error) {
        console.error('Erro ao carregar itens:', error);
        document.getElementById('lista-todos').innerHTML = '<p class="loading">Erro ao carregar dados</p>';
    }
}

// Carregar itens disponiveis
async function carregarDisponiveis() {
    try {
        const response = await fetch(`${API_URL}/api/itens`);
        const apiResponse = await response.json();
        const itens = (apiResponse.dados || []).filter(item => !item.emprestado);
        renderizarItens(itens, 'lista-disponiveis', false);
    } catch (error) {
        console.error('Erro ao carregar disponiveis:', error);
        document.getElementById('lista-disponiveis').innerHTML = '<p class="loading">Erro ao carregar dados</p>';
    }
}

// Carregar itens emprestados
async function carregarEmprestados() {
    try {
        const response = await fetch(`${API_URL}/api/itens`);
        const apiResponse = await response.json();
        const itens = (apiResponse.dados || []).filter(item => item.emprestado);
        renderizarItens(itens, 'lista-emprestados', false);
    } catch (error) {
        console.error('Erro ao carregar emprestados:', error);
        document.getElementById('lista-emprestados').innerHTML = '<p class="loading">Erro ao carregar dados</p>';
    }
}

// Renderizar itens na tela
function renderizarItens(itens, containerId, mostrarAcoes) {
    const container = document.getElementById(containerId);
    
    if (itens.length === 0) {
        container.innerHTML = '<p class="loading">Nenhum item encontrado</p>';
        return;
    }
    
    container.innerHTML = itens.map(item => `
        <div class="item-card ${item.tipo.toLowerCase()}">
            <span class="item-tipo ${item.tipo.toLowerCase()}">${item.tipo}</span>
            <div class="item-titulo">${item.titulo}</div>
            <div class="item-codigo">${item.codigo}</div>
            <div class="item-detalhes">${item.detalhes}</div>
            ${mostrarAcoes ? `
                <div class="item-status">
                    <span class="status-badge ${item.emprestado ? 'emprestado' : 'disponivel'}">
                        ${item.emprestado ? 'EMPRESTADO' : 'DISPONIVEL'}
                    </span>
                    ${!item.emprestado ? 
                        `<button class="item-button btn-emprestar" onclick="emprestar('${item.codigo}')">Emprestar</button>` :
                        `<button class="item-button btn-devolver" onclick="devolver('${item.codigo}')">Devolver</button>`
                    }
                </div>
            ` : ''}
        </div>
    `).join('');
}

// Emprestar item
async function emprestar(codigo) {
    try {
        const response = await fetch(`${API_URL}/api/item/emprestar`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ id: codigo })
        });
        
        const result = await response.json();
        
        if (result.sucesso) {
            mostrarMensagem(result.mensagem || 'Item emprestado com sucesso!', 'sucesso');
            await recarregarTudo();
        } else {
            mostrarMensagem(result.mensagem || 'Erro ao emprestar item', 'erro');
        }
    } catch (error) {
        mostrarMensagem('Erro ao emprestar item', 'erro');
    }
}

// Devolver item
async function devolver(codigo) {
    try {
        const response = await fetch(`${API_URL}/api/item/devolver`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ id: codigo })
        });
        
        const result = await response.json();
        
        if (result.sucesso) {
            mostrarMensagem(result.mensagem || 'Item devolvido com sucesso!', 'sucesso');
            await recarregarTudo();
        } else {
            mostrarMensagem(result.mensagem || 'Erro ao devolver item', 'erro');
        }
    } catch (error) {
        mostrarMensagem('Erro ao devolver item', 'erro');
    }
}

// Recarregar todos os dados
async function recarregarTudo() {
    await carregarTodos();
    await carregarDisponiveis();
    await carregarEmprestados();
}

// Mostrar mensagem de feedback
function mostrarMensagem(texto, tipo) {
    const mensagem = document.getElementById('mensagem');
    mensagem.textContent = texto;
    mensagem.className = `mensagem ${tipo}`;
    mensagem.style.display = 'block';
    
    setTimeout(() => {
        mensagem.style.display = 'none';
    }, 3000);
}
