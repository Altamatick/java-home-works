// Simple calculator logic without using eval() for safety
// We'll implement a tokenizer + shunting-yard to parse infix expressions and evaluate them.

const expressionEl = document.getElementById('expression');
const resultEl = document.getElementById('result');

let expr = '';

function updateDisplay() {
  expressionEl.textContent = expr === '' ? '0' : expr;
  try {
    if (expr.trim() === '') { resultEl.innerHTML = '&nbsp;'; return; }
    const val = evaluateExpression(expr);
    if (val === Infinity || val === -Infinity || Number.isNaN(val)) {
      resultEl.textContent = 'Помилка';
    } else {
      resultEl.textContent = formatNumber(val);
    }
  } catch (e) {
    resultEl.textContent = '';
  }
}

function appendToExpr(s) {
  // prevent invalid double operators (except unary minus after operator or start)
  if (s.match(/[0-9.]/)) {
    expr += s;
  } else if (s === '(') {
    // allow '(' after digit or operator
    expr += s;
  } else if (s === ')') {
    expr += s;
  } else if (s.match(/[+\-*/]/)) {
    if (expr === '' && s === '-') {
      expr += s; // allow leading unary minus
    } else if (expr === '') {
      return; // don't allow other leading operators
    } else {
      // replace last operator with new one if last is operator (except ) )
      const last = expr[expr.length-1];
      if (last.match(/[+\-*/]/)) {
        expr = expr.slice(0,-1) + s;
      } else {
        expr += s;
      }
    }
  }
  updateDisplay();
}

function backspace() {
  if (expr.length > 0) {
    expr = expr.slice(0, -1);
    updateDisplay();
  }
}

function clearAll() {
  expr = '';
  updateDisplay();
}

function handleEquals() {
  if (expr.trim() === '') return;
  try {
    const val = evaluateExpression(expr);
    if (val === Infinity || val === -Infinity || Number.isNaN(val)) {
      resultEl.textContent = 'Помилка';
    } else {
      expr = String(val);
      updateDisplay();
    }
  } catch (e) {
    resultEl.textContent = 'Невірний вираз';
  }
}

// Basic number formatting to avoid long fractions
function formatNumber(n) {
  if (!isFinite(n)) return String(n);
  if (Math.abs(n) < 1e12 && Number.isInteger(n)) return String(n);
  // limit to 12 significant digits
  return Number.parseFloat(n.toPrecision(12)).toString();
}

// Tokenizer: numbers, operators, parentheses
function tokenize(s) {
  const tokens = [];
  let i = 0;
  while (i < s.length) {
    const ch = s[i];
    if (ch === ' ') { i++; continue; }
    if (/[0-9.]/.test(ch)) {
      let j = i+1;
      while (j < s.length && /[0-9.]/.test(s[j])) j++;
      tokens.push({type:'number', value: s.slice(i,j)});
      i = j;
      continue;
    }
    if (ch === '+' || ch === '-' || ch === '*' || ch === '/') {
      tokens.push({type:'op', value: ch});
      i++; continue;
    }
    if (ch === '(' || ch === ')') {
      tokens.push({type:'paren', value: ch});
      i++; continue;
    }
    // unknown char
    throw new Error('Unknown token: ' + ch);
  }
  return tokens;
}

// Shunting yard to convert to RPN
function toRPN(tokens) {
  const out = [];
  const stack = [];

  const prec = { '+':1, '-':1, '*':2, '/':2 };
  const isLeft = { '+':true, '-':true, '*':true, '/':true };

  for (let i = 0; i < tokens.length; i++) {
    const t = tokens[i];
    if (t.type === 'number') out.push(t);
    else if (t.type === 'op') {
      // handle unary minus: if at start or previous token is op or '('
      if (t.value === '-' && (i === 0 || (tokens[i-1].type === 'op' || (tokens[i-1].type === 'paren' && tokens[i-1].value === '(')))) {
        // convert unary minus to a 0 and binary - (i.e., push number 0 before)
        out.push({type:'number', value:'0'});
      }
      while (stack.length > 0) {
        const top = stack[stack.length-1];
        if (top.type === 'op' && ((isLeft[t.value] && prec[t.value] <= prec[top.value]) || (!isLeft[t.value] && prec[t.value] < prec[top.value]))) {
          out.push(stack.pop());
        } else break;
      }
      stack.push(t);
    } else if (t.type === 'paren') {
      if (t.value === '(') stack.push(t);
      else {
        // right paren
        while (stack.length > 0 && !(stack[stack.length-1].type === 'paren' && stack[stack.length-1].value === '(')) {
          out.push(stack.pop());
        }
        if (stack.length === 0) throw new Error('Mismatched parentheses');
        stack.pop(); // pop '('
      }
    }
  }

  while (stack.length > 0) {
    const t = stack.pop();
    if (t.type === 'paren') throw new Error('Mismatched parentheses');
    out.push(t);
  }
  return out;
}

function evaluateRPN(rpn) {
  const st = [];
  for (const t of rpn) {
    if (t.type === 'number') {
      if (t.value.split('.').length > 2) throw new Error('Invalid number');
      st.push(Number(t.value));
    } else if (t.type === 'op') {
      const b = st.pop();
      const a = st.pop();
      if (a === undefined || b === undefined) throw new Error('Invalid expression');
      let res;
      switch (t.value) {
        case '+': res = a + b; break;
        case '-': res = a - b; break;
        case '*': res = a * b; break;
        case '/':
          if (b === 0) throw new Error('Division by zero');
          res = a / b; break;
        default: throw new Error('Unknown op');
      }
      st.push(res);
    }
  }
  if (st.length !== 1) throw new Error('Invalid expression');
  return st[0];
}

function evaluateExpression(s) {
  const tokens = tokenize(s);
  const rpn = toRPN(tokens);
  return evaluateRPN(rpn);
}

// wire up buttons
document.querySelectorAll('.btn').forEach(btn => {
  btn.addEventListener('click', () => {
    const v = btn.dataset.value;
    const action = btn.dataset.action;
    if (action === 'clear') return clearAll();
    if (action === 'backspace') return backspace();
    if (action === 'equals') return handleEquals();
    if (v !== undefined) appendToExpr(v);
  });
});

// keyboard support
window.addEventListener('keydown', (e) => {
  if ((e.key >= '0' && e.key <= '9') || e.key === '.') {
    appendToExpr(e.key);
    e.preventDefault();
  } else if (['+','-','*','/','(',')'].includes(e.key)) {
    appendToExpr(e.key);
    e.preventDefault();
  } else if (e.key === 'Enter') {
    handleEquals(); e.preventDefault();
  } else if (e.key === 'Backspace') {
    backspace(); e.preventDefault();
  } else if (e.key === 'Escape') {
    clearAll(); e.preventDefault();
  }
});

// initialize
updateDisplay();
