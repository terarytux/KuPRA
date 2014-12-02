INSERT INTO public.unit (id, name, abbreviation) VALUES (1, 'Random', 'rnd');
INSERT INTO public.unit (id, name, abbreviation) VALUES (2, 'Pixelis', 'px');
INSERT INTO public.unit (id, name, abbreviation) VALUES (3, 'Kilogramas', 'Kg');
INSERT INTO public.unit (id, name, abbreviation) VALUES (4, 'Litras', 'l');


INSERT INTO public.product (id, name, selected_unit, description) VALUES (1, 'Mėsa', 3, 'Raudonas daiktas');
INSERT INTO public.product (id, name, selected_unit, description) VALUES (2, 'Pienas', 4, 'Baltas skystis iš karvės');

INSERT INTO public.fridge (id, user_id, product_id, amount) VALUES (1, 'Hudas', 1, 2);
INSERT INTO public.fridge (id, user_id, product_id, amount) VALUES (2, 'Hudas', 2, 3);





